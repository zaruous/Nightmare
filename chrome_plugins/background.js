

chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  if (request.action === 'sendHtml' || request.action === 'sendHtmlNavigation' || request.action === 'sendHtml') {
    fetch('http://localhost:10022/getHtml',{
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ eventName: request.action , html: request.html })
    })
    /*.then(response => response.json())*/
    .then(data => console.log(data))
	 .catch(error => {
      console.error(error);
      //sendResponse({ success: false, error: error });
    });
  }
  
});


chrome.webNavigation.onCompleted.addListener(details => {
  chrome.scripting.executeScript({
    target: { tabId: details.tabId },
    function: sendHtmlNavigation
  });
});
function sendHtmlNavigation() {
  const html = document.body.outerHTML;
  const filteredHtml = html.replace(/<script[^>]*>([\s\S]*?)<\/script>/gi, '');
  
  chrome.runtime.sendMessage({ action: 'sendHtmlNavigation', html: filteredHtml });
}


function sendHtml() {
  const html = document.body.outerHTML;
  chrome.runtime.sendMessage({ action: 'sendHtml', eventName:"webNavigation", html: html });
}
/*
*/