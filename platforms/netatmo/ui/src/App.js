import React from 'react';
import './App.css';

function Root() {
  return (
    <span>Root</span>
  );
}

function Tacos() {
  return (
    <span>Tacos</span>
  );
}


function App(Router) {
  return (
    <div className="netatmo">
      <Router.Switch>
        <Router.Route exact path="/" component={Root}/>
        <Router.Route exact path="/tacos" component={Tacos}/>
      </Router.Switch>
    </div>
  );
}

export default (Router)=>{
  return ()=>{
    return App(Router);
  }
}
