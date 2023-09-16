import React from "react";

const DifferenceTable = (props) => {

    const tableRows = props.data.map(
        (row, idx) =>
            <tr key={idx}>
                <th>{row.differences[0]}</th>
                <th>{row.differences[1]}</th>
                <th>{row.differences[2]}</th>
                <th>{row.differences[3]}</th>
            </tr>
    );

    console.log('DifferenceTable ->', props.data);

    return (
        <>
            <table className="table">
                <thead>
                {tableRows}
                </thead>
            </table>
        </>

    );
}

export default DifferenceTable;