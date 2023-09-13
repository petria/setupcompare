import axios from "axios";

const API_URL = "http://localhost:8080/api";

const client = axios.create({
    baseURL: API_URL
});

const getTrackListForCar = (carFolderName, ok, error) => {
    const url = "/setups/track-list-for-car";
    console.log('POST url: ', url);
    client.post(url, {
        carFolderName: carFolderName
    }).then(
        (response) => {
            ok(response)
        }
    )
        .catch((err) => {
                error(err);
            }
        );

}

const scanForSetupIniFiles = (ok, error) => {
    const url = "/setups/scan-for-setup-ini-files";
    console.log('GET url: ', url);
    client.get(url).then(
        (response) => {
            ok(response)
        }
    )
        .catch((err) => {
                error(err);
            }
        );
}


const getCarListForSelection = (ok, error) => {
    const url = "/setups/car-list-for-selection";
    console.log('GET url: ', url);
    client.get(url)
        .then(
            (response) => {
                ok(response);
            }
        )
        .catch((err) => {
                error(err)
            }
        );
}

const SetupDataFetcher = {
    getCarListForSelection,
    getTrackListForCar,
    scanForSetupIniFiles
}

export default SetupDataFetcher;