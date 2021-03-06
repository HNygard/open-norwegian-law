var searchBox = document.getElementById('law-ref-search');

// :: Read from query parameter
var url_string = window.location.href;
var url = new URL(url_string);
var lawRefFromParam = url.searchParams.get('lawRef');
if (lawRefFromParam) {
    console.log('Found law reference in param: ' + lawRefFromParam);
    searchBox.value = lawRefFromParam;
}

var currentSearch = null;
var search = function() {
    var value = searchBox.value;
    if (value !== currentSearch) {
        currentSearch = value;

        // :: Update URL
        var pageUrl = '?lawRef=' + currentSearch;
        window.history.pushState('', '', pageUrl);

        // :: Call API
        fetch('./api/law-reference?searchQuery=' + value)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                console.log(data);
                if (data.status && data.status === 500) {
                    document.getElementById('law-ref-result-info').innerHTML = '';
                    document.getElementById("law-ref-result").innerHTML = "<div class='alert alert-danger'><b>" + data.message + "</b><br><br>" +
                        "<pre>" + data.trace + "</pre></div>";
                } else {
                    var resultInfo = '<b>Match found:</b> ' + data.lawReference + '&nbsp;&nbsp;&nbsp;<b>Match type:</b>';
                    data.lawReferenceMatchTypes.forEach((matchType) => {
                        resultInfo += ' ' + matchType;
                    });
                    document.getElementById('law-ref-result-info').innerHTML = resultInfo;
                    document.getElementById("law-ref-result").innerHTML = "<div class='law-ref-result-success'>" + data.html + "</div>";
                }
            });
    }
};

searchBox.onkeyup = search;
searchBox.onchange = search;
search();
