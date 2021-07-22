var fetchLaws = function () {
    document.getElementById('laws-result-info').innerHTML = '<i>(Fetching laws from system... )</i>';
    // :: Call API
    fetch('./api/laws')
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            console.log(data);
            if (data.status && data.status === 500) {
                document.getElementById('laws-result-info').innerHTML = '';
                document.getElementById("laws-result").innerHTML = "<div class='alert alert-danger'><b>" + data.message + "</b><br><br>" +
                    "<pre>" + data.trace + "</pre></div>";
            } else {
                var resultInfo = '<i>Laws fetched:</i>';
                var resultHtml = '';
                data.laws.forEach((law) => {
                    resultHtml += ' <li >'
                        + law.lawId + ' - '
                        + '<a href="./law-reference?lawRef=' + law.lawId + '" ' + (law.changeLaw ? ('style="color: gray"') : '') + '>'
                        + law.fullName
                        + '</a>';

                    if (law.changeInLawName) {
                        resultHtml += '<ul style="margin: 0;"><li>CHANGE IN -- '
                        + law.changeInLawName + ' - '
                        + ((law.changeInLawId)
                                ? '<a href="./law-reference?lawRef=' + law.changeInLawId + '">' + law.changeInLawId + '</a>'
                                : '<i style="color: red;">Law not found.</i>')
                        + '</li></ul>';
                    }
                    resultHtml += '</li>';
                });
                document.getElementById('laws-result-info').innerHTML = resultInfo;
                document.getElementById("laws-result").innerHTML = "<div class='law-ref-result-success'>" + resultHtml + "</div>";
            }
        });
};

fetchLaws();
