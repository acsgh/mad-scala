import React, {Component} from 'react';
import Nav from "./Nav.jsx";

export const GLOBAL_PREFIX = "mad";

export default class Index extends Component {

    constructor(props) {
        super(props);

        this.state = {
        };
    }

    render() {
        return (
            <div className={GLOBAL_PREFIX}>
                <Nav/>
                <main role="main" className="container">
                    <div className="jumbotron">
                        <h1>Navbar example</h1>
                        <p className="lead">This example is a quick exercise to illustrate how the top-aligned navbar
                            works. As you scroll, this navbar remains in its original position and moves with the rest
                            of the page.</p>
                        <a className="btn btn-lg btn-primary" href="/docs/4.3/components/navbar/" role="button">View
                            navbar docs &raquo;</a>
                    </div>
                </main>
            </div>


        );
    }
}
