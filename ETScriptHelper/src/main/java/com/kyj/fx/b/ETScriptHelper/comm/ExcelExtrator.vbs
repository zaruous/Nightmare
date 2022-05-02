' Extract Data from Microsoft Excel

outFileName = WScript.Arguments.Item(0)
fromExcel = WScript.Arguments.Item(1)

WScript.Echo outFileName
WScript.Echo fromExcel

Set objConn = CreateObject("ADODB.Connection")
Set oxml = CreateObject("msxml2.domdocument.6.0")
objConn.open "Provider=Microsoft.ACE.OLEDB.12.0; Data Source=" & fromExcel & "; Extended Properties=""Excel 12.0;"";"

Set ors = objConn.Execute("select * from [Sheet0$] ")
on error resume next
oxml.loadXML "<Data/>"
Do While Not(ors.EOF)
	colCount = ors.Fields.Count
	buff = ""
	
	Set oRow = NewRow()
	
	For c = 1 To colCount - 1
		AddColumn oRow, ors.Fields.Item(c).Name, ors(c).Value
	Next
	
	'WScript.Echo buff
	ors.MoveNext
Loop
oxml.save outFileName
ors.Close



Function NewRow()
	Set oRow = oxml.createElement("Row")
	oxml.firstChild.appendChild oRow
	Set NewRow = oRow
End Function

Sub AddColumn(oRow , colName, value)
	Set oNewCol = oxml.createElement(colName)
	oNewCol.text = value
	oRow.appendChild oNewCol
End Sub