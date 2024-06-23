console.log("content.js");
document.addEventListener('DOMContentLoaded', () => {
  console.log("domdocument loaded.");
  const html = document.documentElement.outerHTML;
  chrome.runtime.sendMessage({ action: 'sendHtml', html: html });
});

console.log("content.js end");