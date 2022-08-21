import * as React from 'react';
import Map from './Map';
import {BrowserRouter, Switch, Route} from 'react-router-dom';

import NodeDetails from './NodeDetails'
import {StoreProvider} from "./Store";
import ControllerProvider from "./Controller";
import logo from "./logo.svg";

const App = () => {
        return (
            <BrowserRouter>
                <StoreProvider>
                    <ControllerProvider>
                        <Map/>
                        <div className="absolute top-0 left-0 z-2 ml-3 mt-3">
                            <a href="/">
                                <img src={logo} alt="logo" style={{
                                    height: '40px'
                                }}/>
                            </a>

                            <Switch>
                                <Route exact path="/nodes/:address" component={NodeDetails}/>
                                <Route exact path="/" component={NodeDetails}/>
                            </Switch>
                        </div>
                    </ControllerProvider>
                </StoreProvider>
            </BrowserRouter>
        );
    }
;

export default App;
