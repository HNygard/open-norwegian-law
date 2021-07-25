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

                    if (law.changeInLawNames) {
                        for(var lawId2 in law.changeInLaws) {
                            resultHtml += '<ul style="margin: 0;"><li>CHANGE IN -- '
                                + lawId2
                                + ' - <a href="./law-reference?lawRef=' + law.changeInLaws[lawId2] + '">'
                                + law.changeInLaws[lawId2] + '</a>'
                                + '</li></ul>';
                        }
                    }
                    if (law.debugInformation) {
                        law.debugInformation.forEach(logLine => {
                            resultHtml += '<ul style="margin: 0;"><li>DEBUG INFO -- '
                                + logLine + '</li></ul>';
                        });
                    }
                    if (law.thisLawChangedBy) {
                        resultHtml += '<ul style="margin: 0;">CHANGED BY -- ';
                        for(var lawId in law.thisLawChangedBy) {
                            resultHtml += '<li>' + lawId + ' - ' + law.thisLawChangedBy[lawId] + '</li>';
                        }
                        resultHtml += '</ul>';
                    }
                    resultHtml += '</li>';
                });
                document.getElementById('laws-result-info').innerHTML = resultInfo;
                document.getElementById("laws-result").innerHTML = "<div class='law-ref-result-success'>" + resultHtml + "</div>";
            }
        });
};

fetchLaws();
