import React, {useEffect, useState} from "react";
import axios from "axios";
import CarSelector from "./CarSelector";

const client = axios.create({
    baseURL: "http://localhost:3002/api"
});

const SetupCompare = () => {

    const [error, setError] = useState(null);

    const [data, setData] = useState(null);

    const [callBackData, setCallBackData] = useState(null);

    const [selectionData, setSelectionData] = useState({
        carsList: ['car1', 'car2', 'car3'],
        trackList: ['track1', 'track2', 'track3'],
        setupIniList: ['setupIni', 'setupIni', 'setupIni'],
    })

    const [carList, setCarList] = useState(null);

    useEffect(() => {
        console.log("use effect!")

        async function getSetupData() {
            const url = "http://localhost:8080/api/setups/carList";
            console.log('url: ', url);
            client.get(url)
                .then(
                    (response) => {
                        console.log('response', response);
                        setData(response);
                    }
                )
                .catch((err) => {
                        console.log("Network error: ", err);
                        setError(err.message);
                        setData(null);
                    }
                );

        }

        async function getCarListForSelection() {
            const url = "http://localhost:8080/api/setups/car-list-for-selection";
            console.log('url: ', url);
            client.get(url)
                .then(
                    (response) => {
                        console.log('response', response);
                        setCarList(response.data);
//                        setData(response);
                    }
                )
                .catch((err) => {
                        console.log("Network error: ", err);
                        setError(err.message);
                        setData(null);
                    }
                );

        }

        getCarListForSelection();
//        getSetupData();

    }, []);


    const SetupNamesTable = (props) => {
        const tableRows = props.data.map(
            (setup) =>
                <tr key={setup.name}>
                    <th>{setup.name}</th>
                    <th>{setup.iniFile}</th>
                </tr>
        );

        return (
            <>
                <table className="table table-dark">
                    <thead>
                    {tableRows}
                    </thead>
                </table>
            </>

        );
    }

    const Values = (props) => {
        const values = props.values.map(
            (value) =>
                <div key={value.name} className={value.difference}>{value.name} {value.value}</div>
        );
        return <div>{values}</div>
    }

    const SetupDiffValues = (props) => {
        return props.diffs.map(
            (diff) =>
                <div key={diff.category}>
                    <div>{diff.category}</div>
                    <div><Values values={diff.values}></Values></div>
                </div>
        );
    }

    const carSelectCb = (data) => {
        console.log('callback -> ', data);
        setCallBackData(data);
    }

    const CompareTable = (props) => {

        const tableTds = props.data.setups.map(
            (setup) =>
                <td key={setup.id}>
                    <SetupDiffValues diffs={setup.diffs}></SetupDiffValues>
                </td>
        );

        return (
            <table className="table table-dark">
                <tbody>
                <tr>
                    {tableTds}
                </tr>
                </tbody>

            </table>
        );
    }
    const ShowSelected = (props) => {
        if (props.callBackData === null) {
            return (
                <></>
            )
        } else {
            return (
                <>{props.callBackData}</>
            )
        }
    }


    if (carList !== null) {
        return (
            <div>
                <CarSelector carList={carList} carSelectCb={carSelectCb} selectionData={selectionData}></CarSelector>
                <ShowSelected callBackData={callBackData}></ShowSelected>
            </div>
        )
    } else {
        return (
            <>
                Loading setups...
            </>
        )
    }
}

export default SetupCompare;