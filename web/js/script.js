function cleanElement(el) {
    while (el.firstChild) {
        el.removeChild(el.firstChild);
    }
}
/**
 * Fetches list of all bank statements.
 *
 * @returns {Promise<object[]>} list of entities
 * @throws {Error} when server returns unknown status
 */
async function getAll() {
    const res = await fetch("./bankStatements/");
    if (res.status === 200) {
        return res.json();
    }
    throw new Error(`Unknown status: ${res.statusText} (${res.status})`)
}

/**
 * Puts the fetched result from getAll() method to a HTML table  * 
 */
function showBankStatements() {
    const d = document.getElementById("data");
    cleanElement(d);
    getAll()
            .then(data => {
                const d = document.getElementById("data");
                cleanElement(d);
                const table = document.createElement("table");
                let tr, th, td, button;
                // creating table header
                tr = document.createElement("tr");
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Account number"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Operation date"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Beneficiary"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Comment"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Ammount"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Currency"));
                tr.appendChild(th);
                table.appendChild(tr);
                // creating data table
                for (const row of data) {
                    tr = document.createElement("tr");
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.accountNumber));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.operationDate));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.beneficiary));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.comment));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.amount));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.currency));
                    tr.appendChild(td);
                    table.appendChild(tr);
                }
                d.appendChild(table);
            })
            .catch(err => {
                alert("Failed to load list: " + err.message);
            });
}


