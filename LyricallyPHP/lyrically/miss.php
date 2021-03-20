<?php
include 'connect.php';
if($_POST['page']=='app_notify')
{   
	if($_POST['work']=='delete')
	{
		mysqli_query($con,"delete from notify where id=".$_POST['did']."");
	}
	?>
	<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
		<thead>
			<tr>
				<th>id</th>
				<th>message</th>
				<th>Time</th>
				<th>Delete</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<th>id</th>
				<th>message</th>
				<th>Time</th>
				<th>Delete</th>
			</tr>
		</tfoot>
		<tbody>

			<?php
			$query=mysqli_query($con,"select * from notify");
			if($query)
			{
				while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {
					?>
					<tr>
						<td><?php echo $row["id"];  ?></td>
						<td><?php  echo $row["message"]; ?></td>
						<td><?php  echo $row["ntime"]; ?></td>
						<td>
							<form method="POST">
								<input type="hidden" name="did" value="<?php echo $row['id']; ?>">
								<button type="button" name="delete" onclick="app_notify('notify_data','app_notify','delete','0','0','<?php  echo $row['id']; ?>')">Delete</button>
							</form>
						</td>
					</tr>
					<?php
				}
			}
			?>
		</tbody>
	</table>
	<?php
}
?>