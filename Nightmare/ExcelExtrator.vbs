' Extract Data from Microsoft Excel


outFileName = WScript.Arguments.Item(0)
outExcelFile = WScript.Arguments.Item(1)

Set objConn = CreateObject("ADODB.Connection")

'Set shell = CreateObject( "WScript.Shell" )

objConn.open "Provider=Microsoft.ACE.OLEDB.12.0; Data Source=" & outExcelFile & "; Extended Properties=""Excel 12.0;"";"

Set ors = objConn.Execute("select * from [Sheet0$] ")

Set oxml = CreateObject("msxml2.domdocument.6.0")
oxml.loadXML "<Data/>"
Do While Not(ors.EOF)
	colCount = ors.Fields.Count
	buff = ""
	
	Set oRow = NewRow()
	
	For c = 1 To colCount - 1
		AddColumn oRow, ors.Fields.Item(c).Name, ors(c).Value
		'buff = buff & ors(c).Value  & "\t" 
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
	Dim colName_
	colName_ = Replace( colName , " ", "_")
	Set oNewCol = oxml.createElement(colName_)
	oNewCol.text = value
	oRow.appendChild oNewCol
End Sub