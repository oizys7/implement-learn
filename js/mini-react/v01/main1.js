// console.log("mainjs")

// 1. 代码硬编码
// const dom = document.createElement("div")
// dom.id = "app"
// document.querySelector('#root').append(dom)

// const textNode = document.createTextNode("")
// textNode.nodeValue = "app"
// dom.append(textNode)


// 2. react -> v-dom -> js object
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


// const dom = document.createElement(el.type)
// dom.id = el.props.id
// document.querySelector('#root').append(dom)

// const textNode = document.createTextNode("")
// textNode.nodeValue = textEl.props.nodeValue
// dom.append(textNode)


// 3. 动态创建dom
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
            children: children.map(c => {
                return typeof c === "string" ? createTextNode(c) : c
            })
        }
    }
}

//
// const textEl = createTextNode("app")
// const App = createElement("div", {id: "app"}, textEl)
//
//
// const dom = document.createElement(App.type)
// dom.id = App.props.id
// document.querySelector('#root').append(dom)
//
// const textNode = document.createTextNode("")
// textNode.nodeValue = textEl.props.nodeValue
// dom.append(textNode)

// 这里createTextNode、createElement两个函数的目的都是去创建一个节点，可以想一种更加抽象的函数，完成这个动作
// a. 创建节点
// b. 添加属性
// c. 在对应父节点中添加改子节点

function render(el, container) {
    const dom =
        el.type === "TEXT_ELEMENT"
            ? document.createTextNode("")
            : document.createElement(el.type)

    Object.keys(el.props)
        .filter(key => key !== "children")
        .forEach(key => {
            dom[key] = el.props[key]
        })

    const children = el.props.children
    children.forEach(child => render(child, dom))

    container.appendChild(dom)
}


// const App = createElement("div",
//     {id: "app"}, "app",)
// render(App, document.querySelector("#root"))

// 实现 react api

const ReactDOM = {
    createRoot(container) {
        return {
            render(App) {
                render(App, container)
            }
        }
    }
}

const App = createElement("div", {id: "app"}, "app11",)
ReactDOM.createRoot(document.querySelector('#root')).render(App)