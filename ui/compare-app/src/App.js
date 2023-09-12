import React, {useEffect} from "react";
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-notifications/lib/notifications.css';
import RootComponent from "./components/RootComponent";
import {NotificationContainer, NotificationManager} from "react-notifications";
import EventBus from "./common/EventBus";

const App = () => {

    useEffect(() => {

        EventBus.on("notify_test", (data) => {

            if (data.type === 'ERROR') {

                NotificationManager.error(data.message, data.title);

            } else if (data.type === 'WARNING') {

                NotificationManager.warning(data.message, data.title);

            } else {

                NotificationManager.info(data.message, data.title);

            }

        });

        return () => {
            EventBus.remove("notify_test");
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <div>
            <NotificationContainer/>
            <RootComponent></RootComponent>
        </div>
    );
}

export default App;
