// Function to convert table to CSV format
function tableToCSV(table) {
  var rows = table.querySelectorAll('tr');
    var csv = [];
    
    for (var i = 0; i < rows.length; i++) {
        var row = [];
        var cols = rows[i].querySelectorAll('td, th');  // Get cells of the current row
        
        for (var j = 0; j < cols.length; j++) {
            row.push('"' + cols[j].innerText.replace(/"/g, '""') + '"');
        }
        
        csv.push(row.join(','));	
    }
    
    return csv.join('\n');
}

// Function to extract text and convert tables
function extractContent() {
    var content = "";

    // Remove all script elements
    var scripts = document.querySelectorAll('script');
    scripts.forEach(function(script) {
        script.remove();
    });

    // Extract text and convert tables
    document.body.childNodes.forEach(function(node) {
        if (node.nodeType === Node.TEXT_NODE) {
            content += node.textContent.trim() + ' ';
        } else if (node.nodeType === Node.ELEMENT_NODE) {
            if (node.tagName.toLowerCase() === 'table') {
                content += '\n' + tableToCSV(node) + '\n';
            } else {
                content += node.innerText.trim() + ' ';
            }
        }
    });

    return content.trim();
}

// Execute the extraction and log the result
extractContent();