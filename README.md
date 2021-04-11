# Code-Pairing-Assignment-TradeStore

<h2>Problem Statement</h2><br/>
There is a scenario where thousands of trades are flowing into one store, assume any way of transmission of trades. We need to create a one trade store, which stores the trade in the following order:

<table>
<tr>
<th>Trade Id</th>
<th>Version</th>
<th>Counter-Party Id</th>
<th>Book-Id</th>
<th>Maturity Date</th>
<th>Created Date</th>
<th>Expired</th>
</tr>
<tr>
<td>T1</td>
<td>1</td>
<td>CP-1</td>
<td>B1</td>
<td>20/05/2020</td>
<td>&lt;today date&gt;</td>
<td>N</td>
</tr>
<tr>
<td>T2</td>
<td>2</td>
<td>CP-2</td>
<td>B1</td>
<td>20/05/2021</td>
<td>&lt;today date&gt;</td>
<td>N</td>
</tr>
<tr>
<td>T2</td>
<td>1</td>
<td>CP-1</td>
<td>B1</td>
<td>20/05/2021</td>
<td>14/03/2015</td>
<td>N</td>
</tr>
<tr>
<td>T3</td>
<td>3</td>
<td>CP-3</td>
<td>B2</td>
<td>20/05/2014</td>
<td>&lt;today date&gt;</td>
<td>Y</td>
</tr>
</table>

There are couples of validation, we need to provide in the above assignment
1.	During transmission if the lower version is being received by the store it will reject the trade and throw an exception. If the version is same it will override the existing record.
2.	Store should not allow the trade which has less maturity date then today date.
3.	Store should automatically update expire flag if in a store the trade crosses the maturity date.
