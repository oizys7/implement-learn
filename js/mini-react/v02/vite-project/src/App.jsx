// /**@jsx React.createElement*/
import React from './core/React.js'

const App = <div>111</div>

function AppOne() {
    return (
        <div>
            <h1 id="title">Hello World</h1>
            <button onClick={() => {
                console.log('click')
            }}>Click
            </button>
        </div>
    )
}

export default App