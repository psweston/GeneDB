<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="db" uri="db" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script src="<c:url value="/includes/scripts/script.aculo.us/prototype.js"/>" type="text/javascript"></script>
<script src="<c:url value="/includes/scripts/script.aculo.us/scriptaculous.js"/>" type="text/javascript"></script>
<style type="text/css">.infoMacro { border-style: solid; border-width: 1px; border-color: #c0c0c0; background-color: #ffffff; text-align:left;}.informationMacroPadding { padding: 5px 0 0 5px; }</style>
<script type="text/javascript">
<!--
function hideSection(sectionId) {
  	document.getElementById(sectionId).style.display='none';
  	document.getElementById(sectionId).style.visibility='hidden';
}

function showSection(sectionId) {
  	document.getElementById(sectionId).style.display='block';
  	document.getElementById(sectionId).style.visibility='visible';
}
//-->
</script>

<format:header name="History Download">
	<st:init />
	<link rel="stylesheet" href="<c:url value="/"/>includes/style/alternative.css" type="text/css"/>
</format:header>

<form:form commandName="downloadOptions" action="DownloadFeatures" method="POST">

<div class='informationMacroPadding' align="center">
<table cellpadding='5' width='90%' cellspacing='0' class='infoMacro' border='0'>
<tr><td>
<h3>Select data for download</h3>
<table width="100%" border="0">
<tr bgcolor="FAFAD2">
<td width="33%"><form:checkbox path="outputOption" value="ORGANISM"/>Organism</td>
<td width="33%"><form:checkbox path="outputOption" value="SYS_ID"/>Systematic ID</td>
<td width="33%"><form:checkbox path="outputOption" value="PRIMARY_NAME"/>Primary Name</td>
</tr>
<tr>
<td width="33%"><form:checkbox path="outputOption" value="PRODUCT"/>Product</td>
<td width="33%"><form:checkbox path="outputOption" value="SYNONYMS"/>Synonyms</td>
<td width="33%"><span id="fieldOptionsOpener"><a href="javascript:showSection('fieldOptions');hideSection('fieldOptionsOpener')">See more options</a></span></td>
</tr>
</table>
<div id="fieldOptions" style="visibility: hidden; display: none">
<table width="100%">
<tr bgcolor="FAFAD2">
<td width="33%"><form:checkbox path="outputOption" value="PREV_SYS_ID"/>Previous Systematic ID</td>
<td width="33%"><form:checkbox path="outputOption" value="CHROMOSOME"/>Chromosome</td>
<td width="33%"><form:checkbox path="outputOption" value="LOCATION"/>Location (coordinates)</td>
</tr>
<tr>
<td width="33%"><form:checkbox path="outputOption" value="EC_NUMBERS"/>EC Numbers</td>
<td width="33%"><form:checkbox path="outputOption" value="NUM_TM_DOMAINS"/>No. of TM domains</td>
<td width="33%"><form:checkbox path="outputOption" value="SIG_P"/>Presence of signal peptide</td>
</tr>
<tr bgcolor="FAFAD2">
<td width="33%"><form:checkbox path="outputOption" value="GPI_ANCHOR"/>Presence of GPI anchor</td>
<td width="33%"><form:checkbox path="outputOption" value="MOL_WEIGHT"/>Molecular weight</td>
<td width="33%"><form:checkbox path="outputOption" value="ISOELECTRIC_POINT"/>Isoelectric point</td>
<tr>
<td width="33%"><form:checkbox path="outputOption" value="GO_IDS"/>GO IDs</td>
<td width="33%"><form:checkbox path="outputOption" value="PFAM_IDS"/>Pfam IDs</td>
<td width="33%"><form:checkbox path="outputOption" value="INTERPRO_IDS"/>Interpro IDs</td>
</tr>
</table>
<span id="fieldOptionsOpener"><a href="javascript:showSection('fieldOptionsOpener');hideSection('fieldOptions')">Hide extra options</a></span>
</div>
<p><b>Sequence Options</b>
<table width="100%">
<tr><td><INPUT type="checkbox" NAME="sequenceType" VALUE="3">Protein sequence</td></tr>
<tr><td><INPUT type="checkbox" NAME="sequenceType" checked VALUE="1">Nucleotide sequence of CDS (and introns)</td></tr>
</table>
<div id="sequenceOptionsOpener"><a href="javascript:showSection('sequenceOptions');hideSection('sequenceOptionsOpener')">Extra sequence options</a></div>
<div id="sequenceOptions" style="visibility: hidden; display: none">
<table width="100%">
<tr><td colspan="3"><INPUT type="checkbox" NAME="sequenceType" VALUE="2">Nucleotide (without introns)</td></tr>
<tr><td colspan="3"><INPUT type="checkbox" NAME="sequenceType" VALUE="2">5' UTR</td></tr>
<tr><td colspan="3"><INPUT type="checkbox" NAME="sequenceType" VALUE="2">3' UTR</td></tr>
<tr bgcolor="FAFAD2"><td><INPUT type="checkbox" NAME="sequenceType" VALUE="6">Intergenic Sequence (5'&nbsp;)</td>
<td rowspan="3" align="left">
<input type="hidden" name="includeRNA" value="false">
&nbsp;&nbsp;&nbsp;Number of bases: 
<select name="primeX" >
<option>20</option>
<option>50</option>
<option>100</option>
<option>150</option>
<option>200</option>
<option>300</option>
<option>500</option>
<option>1000</option>
<option>2000</option>
<option value="-1">To next CDS/RNA</option>
</select>
</td></tr>

<tr bgcolor="FAFAD2">
<td>
<input type="hidden" name="includeRNA" value="false">
<INPUT type="checkbox" NAME="sequenceType" VALUE="5">Intergenic Sequence (3' )</td>
</tr>

<tr><td><INPUT type="checkbox" NAME="sequenceType" VALUE="4">CDS/RNA with 5'/3' flanking sequence</td>

<td align="left">
<input type="hidden" name="includeRNA" value="true">
&nbsp;&nbsp;&nbsp;5' distance: 
<select name="prime5" >
<option>0</option>
<option>20</option>
<option>50</option>
<option>100</option>
<option>150</option>
<option>200</option>
<option>300</option>
<option>500</option>
<option>1000</option>
<option>2000</option>
<option value="-1">To next CDS/RNA</option>
</select>
&nbsp;&nbsp;&nbsp;3' distance: 
<select name="prime3" >
<option>0</option>
<option>20</option>
<option>50</option>
<option>100</option>
<option>150</option>
<option>200</option>
<option>300</option>
<option>500</option>
<option>1000</option>
<option>2000</option>
<option value="-1">To next CDS/RNA</option>
</select>
</td></tr>
<tr bgcolor="FAFAD2"><td colspan="3"><INPUT type="checkbox" NAME="sequenceType" VALUE="98">Intron sequence</td></tr>
<tr><td><a href="javascript:showSection('sequenceOptionsOpener');hideSection('sequenceOptions')">Hide extra sequence options</a></td></tr></table>
</td></tr></table></div>

<div class='informationMacroPadding' align="center">
<table cellpadding='5' width='90%' cellspacing='0' class='infoMacro' border='0'>
<tr><td>
<h3>Output Format</h3>
<table width="100%">
<tr>
<td><form:radiobutton path="outputFormat" value="CSV" />"Tab"-delimited file</td>
<td><form:radiobutton path="outputFormat" value="EXCEL" />Excel (.XLS) format</td>
<td><form:radiobutton path="outputFormat" value="ODF" />OpenDocument spreadsheet format</td>
<td><form:radiobutton path="outputFormat" value="HTML" />HTML Table</td>
<td><form:radiobutton path="outputFormat" value="FASTA" />FASTA</td>
</tr>
</table>
</td></tr></table></div>


<div class='informationMacroPadding' align="center">
<table cellpadding='5' width='90%' cellspacing='0' class='infoMacro' border='0'>
<tr><td>
<h3>Select display options</h3>
<p id="displayOptionsOpener">Sensible defaults have been chosen for this section. If you want fine-control 
of the output separators etc, <a href="javascript:showSection('displayOptions');hideSection('displayOptionsOpener')">see options</a></p> 

<div id="displayOptions" style="visibility: hidden; display: none">
<p><a href="javascript:showSection('displayOptionsOpener');hideSection('displayOptions')">Hide this section</a></p>
<table width="100%">
<tr bgcolor="FAFAD2">
<td>Column Headers:</td>

<td><input type="radio" name="cust_header" value="yes">Yes</td>
<td><input type="radio" name="cust_header" value="no" checked>No</td>
<td><small>Applies to tab-delimited and HTML formats</small></td>
</tr>
<tr>
<td>Separator between columns</td>
<td colspan="2"><select name="field_sep">
<option value="default" selected>Default</option>
<option value="tab">TAB</option>
<option value="|">|</option>

<option value=",">,</option>
</select></td>
<td><small>Applies to tab-delimited (default TAB) and FASTA (default |) formats</small></td>
</tr>
<tr bgcolor="FAFAD2">
<td>Empty columns:</td>
<td colspan="2"><select name="field_blank">
<option value="blank" selected>EMPTY</option>
<option value="-">-</option>
</select></td>
<td><small>Applies to tab-delimited and FASTA formats</small></td>

</tr>
<tr>
<td>Separator within columns:</td>
<td colspan="2"><select name="field_intsep">
<option value="," selected>,</option>
<option value="|">|</option>
</select></td>
<td><small>Applies to all formats</small></td>
</tr>
</table>
</div>
</td></tr></table></div>

<div class='informationMacroPadding' align="center">
<table cellpadding='5' width='90%' cellspacing='0' class='infoMacro' border='0'>
<tr><td>
<h3>Output destination</h3>
<p>Display in the browser window is the default output destination. The "Save as..." option will force your browser to save the file directly. 

<P>An option to have download results e-mailed directly, is included.</p>
<table width="100%">
<tr bgcolor="FAFAD2">

<td><form:radiobutton path="outputDestination" value="TO_BROWSER" />&nbsp;To Browser</td>
</tr>
<tr>
<td><form:radiobutton path="outputDestination" value="TO_FILE" />&nbsp;To File...</td>
</tr>
<tr bgcolor="FAFAD2">
<td><form:radiobutton path="outputDestination" value="TO_EMAIL" />&nbsp;To e-mail</td>
</tr>
</table>
</td></tr></table></div>

<div class='informationMacroPadding' align="center">
<table cellpadding='5' width='90%' cellspacing='0' class='infoMacro' border='0'>

<tr><td>
<table width="100%">
<tr>
<td>&nbsp;</td>
<td><input type="submit"></td>
<td>&nbsp;</td>
<td><input type="reset"></td>
<td>&nbsp;</td>
</tr>
</table>
</td></tr></table></div>


<p>&nbsp;</p><p>&nbsp;</p>
<div class='informationMacroPadding' align="center">
<table cellpadding='5' width='90%' cellspacing='0' class='infoMacro' border='0'>
<tr><td>

<h3>Notes</h3>
<ol>
<li><a name="locnote">Location field:</a> For FASTA options the locations will 
be the genomic coordinates.</li>
<li><a name="orth_pasteback">Orthologue pasteback:</a> This option returns the List Download form with the input IDs plus the IDs of all orthologues for those IDs.</li>
<li><a name="orthologue">Orthologues:</a> Orthologue datasets are currently only available for some organisms in GeneDB.</li>
<li><a name="delimeters">Delimiters:</a> The default column separators are the recommended standard. When changing them, bear in mind (i) that the two should differ and (ii) some chosen download fields may contain the delimiter eg. a product may contain a comma.</li>

<li><a name="intra-delimiter">Separator within columns:</a> This denotes the character used to separate items in columns which may contain more than one value eg. synonym.</li>
</ol>
</td></tr></table></div>


</form:form>

<format:footer />