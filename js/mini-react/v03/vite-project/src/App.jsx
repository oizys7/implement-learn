import React from './core/React.js'


function Foo() {
    console.log("re Foo")
    const [count, setCount] = React.useState(10)
    const [bar, setBar] = React.useState("bar")

    React.useEffect(() => {
        console.log("init")
        return () => {
            console.log("cleanup 0")
        }
    }, [])
    React.useEffect(() => {
        console.log("update")
        return () => {
            console.log("cleanup 1")
        }
    }, [count])

    React.useEffect(() => {
        console.log("update")
        return () => {
            console.log("cleanup 2")
        }
    }, [count])

    return (
        <div>
            <div>Foo</div>
            {count}
            <div>{bar}</div>
            <button onClick={() => {
                // setCount((c) => c + 1)
                // setBar((c) => c + "Bar")

                setCount((c) => c + 1)
                setBar((c) => c)
            }}>click
            </button>
        </div>
    )
}

let countBar = 1

function Bar() {
    console.log("Bar")
    const update = React.update()

    React.useEffect(() => {
        console.log("init Bar")
        return () => {
            console.log("cleanup 0")
        }
    }, [])
    return (
        <div>
            <div>Bar</div>
            {countBar}
            <button onClick={() => {
                countBar++
                update()
            }}>click
            </button>
        </div>
    )
}

let count = 1

function App() {
    console.log('App')
    const update = React.update()
    return (
        <div>
            Hello World{count}
            <button onClick={() => {
                count++
                update()
            }}>click
            </button>
            <Foo/>
            <Bar/>

        </div>
    )
}

let showBar = false

function Counter() {
    const foo = <div>
        foo
        <div>child</div>
        <div>child</div>
        <div>child</div>
        <div>11
            <div>child</div>
        </div>
    </div>
    const bar = <p>bar</p>
    return <div>
        counter

        <div>{showBar && bar}</div>

        <button onClick={() => {
            showBar = !showBar
            React.update()
        }}>showBar
        </button>
    </div>
}

export default App