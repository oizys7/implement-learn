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

const React = {
    createElement,
    render
}

export default React