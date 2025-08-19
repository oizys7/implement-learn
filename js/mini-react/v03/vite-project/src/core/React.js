const textEl = {
    type: "TEXT_ELEMENT",
    props: {
        nodeValue: "app11",
        children: []
    }
}

const el = {
    type: "div",
    props: {
        id: "app",
        children: [textEl]
    }
}

function createTextNode(text) {
    return {
        type: "TEXT_ELEMENT",
        props: {
            nodeValue: text,
            children: []
        }
    }
}

function createElement(type, props, ...children) {
    return {
        type: type,
        props: {
            ...props,
            children: children.map(child => {
                return typeof child === "string" || typeof child === "number"
                    ? createTextNode(child) : child
            })
        }
    }
}


function render(el, container) {
    wipRoot = {
        dom: container,
        props: {
            children: [el]
        }
    }
    workOfUnit = wipRoot
}

function update() {
    let curFiber = wipFiber
    return () => {
        wipRoot = {
            ...curFiber,
            alternate: curFiber
        }
        workOfUnit = wipRoot
    }

}

let stateHooks
let stateHookIndex

function useState(initial) {
    let curFiber = wipFiber
    const oldHook = curFiber.alternate?.stateHooks[stateHookIndex]
    const stateHook = {
        state: oldHook ? oldHook.state : initial,
        queue: oldHook ? oldHook.queue : []
    }

    stateHook.queue.forEach(action => {
        stateHook.state = action(stateHook.state)
    })
    stateHook.queue = []

    stateHookIndex++
    stateHooks.push(stateHook)
    curFiber.stateHooks = stateHooks

    function setState(action) {
        const eagerState = action instanceof Function ? action(stateHook.state) : action
        if (stateHook.state === eagerState) {
            return
        }
        stateHook.queue.push(action instanceof Function ? action : () => action)

        wipRoot = {
            ...curFiber,
            alternate: curFiber
        }
        workOfUnit = wipRoot
    }

    return [stateHook.state, setState]
}

let effectHooks

function useEffect(callback, deps) {
    const effectHook = {
        callback,
        cleanup: undefined,
        deps
    }
    effectHooks.push(effectHook)
    wipFiber.effectHooks = effectHooks
}

// 为了解决js的单线程一次性渲染一颗很大的dom树会造成卡顿的问题，所以需要使用
// 小任务分批次渲染，所以应该把dom树处理为一个任务队列方便每一个任务去批量处理
let workOfUnit = null
// work in props
let wipRoot = null
let currentRoot = null
let deletions = []
let wipFiber = null


// 处理完成一起提交
function commitRoot() {
    deletions.forEach(commitDeletion)
    commitWork(wipRoot.child)
    commitEffectHook()
    currentRoot = wipRoot
    wipRoot = null
    deletions = []
}

function commitDeletion(fiber) {
    if (fiber.dom) {
        let parent = fiber.parent;
        while (!parent.dom) {
            parent = parent.parent
        }

        parent.dom.removeChild(fiber.dom)
    } else {
        commitDeletion(fiber.child)
    }
}

function commitWork(fiber) {
    if (fiber) {
        let parent = fiber.parent;
        while (!parent.dom) {
            parent = parent.parent
        }
        if (fiber.effectTag === "placement") {
            if (fiber.dom) {
                parent.dom.append(fiber.dom)
            }
        } else if (fiber.effectTag === "update" && fiber.dom) {
            if (fiber.dom) {
                updateProps(fiber.props, fiber.dom, fiber.alternate?.props)
            }
        }

        commitWork(fiber.child)
        commitWork(fiber.sibling)
    }
}

function commitEffectHook() {
    function run(fiber) {
        if (!fiber) {
            return
        }
        if (!fiber.alternate) {
            fiber.effectHooks?.forEach(hook => {
                // hook.callback()
                hook.cleanup = hook.callback()
            })
        } else {
            // 更新时检查deps是否发生改变
            fiber.effectHooks?.forEach((newHook, newIdx) => {
                if (newHook.deps.length > 0) {
                    const oldHook = fiber.alternate?.effectHooks[newIdx];
                    const needUpdate = oldHook?.deps.some((oldDep, index) => {
                        return oldDep !== newHook.deps[index]
                    })
                    needUpdate && (newHook.cleanup = newHook.callback())
                }
            })
        }
        run(fiber.child)
        run(fiber.sibling)
    }

    function runCleanup(fiber) {
        if (!fiber) {
            return
        }
        fiber.alternate?.effectHooks?.forEach(hook => {
            if (hook.deps.length > 0) {
                hook.cleanup && hook.cleanup()
            }
        })
        runCleanup(fiber.child)
        runCleanup(fiber.sibling)

    }

    runCleanup(wipRoot)
    run(wipRoot)
}

function workLoop(deadline) {
    while (deadline.timeRemaining() > 0 && workOfUnit) {
        // 后续在这里渲染dom
        workOfUnit = performWorkOfUnit(workOfUnit)
        if (wipRoot?.sibling?.type === workOfUnit?.type) {
            workOfUnit = undefined
        }
    }
    if (!workOfUnit && wipRoot) {
        commitRoot()
    }
    if (workOfUnit && !wipRoot) {
        wipRoot = currentRoot
    }
    requestIdleCallback(workLoop)
}

function createDom(type) {
    return type === "TEXT_ELEMENT"
        ? document.createTextNode("")
        : document.createElement(type)
}

function updateProps(nextProps, dom, oldProps) {
    // 1. 老的有，新的没有 -> 删除
    Object.keys(oldProps)
        .filter(key => key !== "children")
        .forEach(key => {
            if (!(key in nextProps)) {
                dom.removeAttribute(key)
            }
        })
    // 2. 老的没有，新的有 -> 添加
    // 3. 老的有，新的有 -> 更新
    Object.keys(nextProps)
        .filter(key => key !== "children" && nextProps[key] !== oldProps[key])
        .forEach(key => {
            if (key.startsWith("on")) {
                const eventType = key.slice(2).toLowerCase()
                dom.removeEventListener(eventType, oldProps[key])
                dom.addEventListener(eventType, nextProps[key])
            } else {
                dom[key] = nextProps[key]
            }
        })
}

function reconcileChildren(fiber, children) {
    let oldFiber = fiber.alternate?.child
    let prevChild = null
    children.forEach((child, index) => {
        let newFiber = null;
        if (oldFiber && oldFiber.type === child.type) {
            newFiber = {
                type: child.type,
                props: child.props,
                parent: fiber,
                sibling: null,
                child: null,
                dom: oldFiber.dom,
                alternate: oldFiber,
                stateHooks: null,
                effectHooks: null,
                effectTag: "update"
            }
        } else {
            if (child) {
                newFiber = {
                    type: child.type,
                    props: child.props,
                    parent: fiber,
                    sibling: null,
                    child: null,
                    dom: null,
                    stateHooks: null,
                    effectHooks: null,
                    effectTag: "placement",
                }
            }
            if (oldFiber) {
                deletions.push(oldFiber)
            }
        }

        if (oldFiber) {
            oldFiber = oldFiber.sibling
        }

        if (index === 0) {
            fiber.child = newFiber
        } else {
            prevChild.sibling = newFiber
        }
        prevChild = newFiber
    })
}

function updateFunctionComponent(fiber) {
    stateHooks = []
    effectHooks = []
    stateHookIndex = 0
    wipFiber = fiber
    const children = [fiber.type(fiber.props)]
    reconcileChildren(fiber, children)
}

function updateHostComponent(fiber) {
    // 1. 创建dom树
    if (!fiber.dom) {
        const dom = fiber.dom = createDom(fiber.type)
        // 2. 处理 props
        updateProps(fiber.props, dom, {})
    }
    const children = fiber.props.children
    reconcileChildren(fiber, children)

}

function performWorkOfUnit(fiber) {

    // 3. 转换为链表
    fiber.type instanceof Function
        ? updateFunctionComponent(fiber)
        : updateHostComponent(fiber)

    // 4. 返回下一个work
    if (fiber.child) {
        return fiber.child
    }

    let next = fiber
    while (next) {
        if (next.sibling) {
            return next.sibling
        }
        next = next.parent
    }
}

requestIdleCallback(workLoop)

const React = {
    createElement,
    render,
    useState,
    useEffect,
    update
}

export default React