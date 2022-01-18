import React from 'react'
import "./Home.css"

export default function Home() {

    return (
        <div className="home">
            {
                [...Array(100).keys()].map((i) =>(
            <div key={i}>
                Welcome Home
            </div>
                ))
            }
            
        </div>
    )
}
