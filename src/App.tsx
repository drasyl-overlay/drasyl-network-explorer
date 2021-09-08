import * as React from 'react';
import Map from './Map';
import {BrowserRouter, Switch, Route} from 'react-router-dom';

import NodeDetails from './NodeDetails'
import {StoreProvider} from "./Store";
import ControllerProvider from "./Controller";

const App = () => {
        return (
            <BrowserRouter>
                <StoreProvider>
                    <ControllerProvider>
                        <Map/>
                        <Switch>
                            <Route exact path="/nodes/:address" component={NodeDetails}/>
                            <Route exact path="/" component={NodeDetails}/>
                        </Switch>
                    </ControllerProvider>
                </StoreProvider>
            </BrowserRouter>
        );
    }
;

export default App;
