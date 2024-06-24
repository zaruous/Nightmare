

chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  if (request.action === 'sendHtml' || request.action === 'sendHtmlNavigation' || request.action === 'sendHtml') {
    fetch('http://localhost:11220/getHtml',{
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ eventName: request.action , text: request.text, location : request.location, iframeSrcs : request.iframeSrcs  })
    })
    /*.then(response => response.json())*/
    .then(data => console.log(data))
	 .catch(error => {
      console.error(error);
    });
  }
  
});


chrome.webNavigation.onCompleted.addListener(details => {
  chrome.scripting.executeScript({
    target: { tabId: details.tabId },
    function: sendHtmlNavigation
  });
});

function getTextWithStructure(element) {
  let text = '';

  function traverse(node, depth = 0) {
    if (node.nodeType === Node.TEXT_NODE) {
      // 텍스트 노드인 경우 공백을 제거하고 추가
      text += node.nodeValue.trim();
    } else if (node.nodeType === Node.ELEMENT_NODE) {
      if (node.tagName === 'BR') {
        text += '\n';
      } else if (node.tagName === 'P' || node.tagName === 'DIV') {
        // P나 DIV 태그인 경우 줄바꿈 추가
        if (text.trim().length > 0) {
          text += '\n';
        }
      } else if (node.tagName === 'TABLE') {
        if (text.trim().length > 0) {
          text += '\n';
        }
      } else if (node.tagName === 'TR') {
        if (text.trim().length > 0) {
          text += '\n';
        }
      } else if (node.tagName === 'TD' || node.tagName === 'TH') {
        text += '\t';
      }

      // 자식 노드를 재귀적으로 탐색
      node.childNodes.forEach(child => traverse(child, depth + 1));
      
      // 테이블 셀의 경우 줄바꿈 추가
      if (node.tagName === 'TD' || node.tagName === 'TH') {
        text += '\t';
      } else if (node.tagName === 'TR') {
        text += '\n';
      }
    }
  }

  traverse(element);
  return text.trim();
}

function sendHtmlNavigation() 
{
	const html = document.documentElement.outerHTML;
	const location = document.location.href
	const filteredHtml = html.replace(/<script[^>]*>([\s\S]*?)<\/script>/gi, '')
			 .replace(/<style[^>]*>([\s\S]*?)<\/style>/gi, '')
			 .replace(/<link[^>]*>([\s\S]*?)<\/link>/gi, '');
  
	// 임시 DOM 요소를 사용하여 텍스트만 추출합니다.
	const tempDiv = document.createElement('div');
	tempDiv.innerHTML = filteredHtml;
	
	let textOnly = tempDiv.innerText || tempDiv.textContent;
	// 비어있는 줄 제거
	textOnly = textOnly.split('\n').filter(line => line.trim() !== '').join('\n');
	//아래 기능이 괜찮지만 플러그인에서 처리안됨.
	//let textOnly = getTextWithStructure(tempDiv);
   
	const iframes = document.getElementsByTagName('iframe');
	const iframeSrcs = [];
	for (var i = 0; i < iframes.length; i++) {
		iframeSrcs.push(iframes[i].src);
	}
  
	chrome.runtime.sendMessage({ action: 'sendHtmlNavigation', text: textOnly, location : location, iframeSrcs : iframeSrcs });
}


function sendHtml() {
  const html = document.body.outerHTML;
  chrome.runtime.sendMessage({ action: 'sendHtml', eventName:"webNavigation", html: html });
}
/*
*/

