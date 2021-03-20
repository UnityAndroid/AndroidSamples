<?php
include 'connect.php';

if($_POST['page']=="save_sound_category")
{
	if($_POST['work']=="show_sound_category")
	{	
		if($_POST['updown']!=0)
		{
			$products=mysqli_query($con,"select * from sound_category order by position asc");
			$i=0;
			$j=0;
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$i++;
				if($pro['category_id']==$_REQUEST['updown'])
				{
					break;
				}
			}
			if($_POST['dir']=="up")
			{
				$products=mysqli_query($con,"select * from sound_category order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i-1))
					{
						break;
					}
				}
			}else{
				$products=mysqli_query($con,"select * from sound_category order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i+1))
					{
						break;
					}
				}
			}

			$k=0;
			$products=mysqli_query($con,"select * from sound_category order by position asc");
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$k++;
				if($j==$k)
				{
					$first=mysqli_query($con,"select * from sound_category where category_id=".$pro['category_id']."");
				}else if($i==$k)
				{
					$second=mysqli_query($con,"select * from sound_category where category_id=".$pro['category_id']."");   
				}
			}

			$row=mysqli_fetch_array($first,MYSQLI_ASSOC);
			$roww=mysqli_fetch_array($second,MYSQLI_ASSOC);

			mysqli_query($con,"UPDATE sound_category SET position=".$row['position']." WHERE category_id=".$roww['category_id']."");  
			mysqli_query($con,"UPDATE sound_category SET position=".$roww['position']." WHERE category_id=".$row['category_id']."");  
		}else if($_POST['did']!=0)
		{
			mysqli_query($con,"delete from sound_category where category_id = ".$_POST['did']."");
		}
		?>
		<div class="card-body">
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
					<thead>
						<tr>
							<th>category_id</th>
							<th>category_name</th> 
							<th>Edit</th>
							<th>Delete</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th>category_id</th>
							<th>category_name</th>
							<th>Edit</th>
							<th>Delete</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</tfoot>
					<tbody>
						<?php
						$i=0;
						$query=mysqli_query($con,"select * from sound_category order by position asc");
						if($query)
						{
							while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {
								$i++;
								?>
								<tr>
									<td><?php echo $row["category_id"];  ?></td>
									<td><?php  echo $row["category_name"]; ?></td>
									<td >
										<form action="save_sound_category.php" method="POST" style="display: flex;align-items: center;align-content: center;">
											<input type="hidden" name="uid" value="<?php echo $row['category_id']; ?>">
											<div style="margin: 0 auto;">
												<input type="submit" name="edit" value="Edit" class="btn btn-primary btn-block">
											</div>
										</form>
									</td>
									<td style="display: flex;align-items: center;align-content: center;">
										<div style="margin: 0 auto;">
											<button class="btn btn-primary btn-block" value="Delete" name="delete" onclick="get_data('save_sound_category','show_sound_category',0,'','<?php echo $row['category_id']; ?>');">
												Delete
											</button>
										</div>
									</td>

									<td><?php  echo $row["position"]; ?></td>
									<td style="display: flex;align-items: center;align-content: center;">
										<div style=" display: table;margin: 0 auto;text-align: center;">
											<?php
											if(mysqli_num_rows($query)>1)
											{
												?>
												<div style="display: table-row;">
													<?php
													if($i!=1)
													{
														?>
														<div style=" display: table-cell;padding: 5px;">
															<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_data('save_sound_category','show_sound_category','<?php echo $row['category_id']; ?>','up',0);">
																<input type="image" src="Images/up.png" />
															</button>
														</div>
														<?php
													}
													?>
													<?php
													if($i!=mysqli_num_rows($query))
													{
														?>
														<div style=" display: table-cell;padding: 5px;">
															<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_data('save_sound_category','show_sound_category','<?php echo $row['category_id']; ?>','down',0);">
																<input type="image" src="Images/down.png" />
															</button>
														</div>
														<?php
													}
													?>
												</div>
												<?php
											}
											?>
										</div>
									</td>
								</tr>
								<?php
							}
						}
						?>
					</tbody>
				</table>
			</div>
		</div>
		<?php
	}
}






if($_POST['page']=="save_video_category")
{
	if($_POST['work']=="show_video_category")
	{

		if($_POST['updown']!=0)
		{
			$products=mysqli_query($con,"select * from video_category order by position asc");
			$i=0;
			$j=0;
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$i++;
				if($pro['Cat_Id']==$_POST['updown'])
				{
					break;
				}
			}
			
			if($_POST['dir']=="up")
			{
				$products=mysqli_query($con,"select * from video_category order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i-1))
					{
						break;
					}
				}
			}else{
				$products=mysqli_query($con,"select * from video_category order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i+1))
					{
						break;
					}
				}
			}


			$k=0;
			$products=mysqli_query($con,"select * from video_category order by position asc");
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$k++;
				if($j==$k)
				{
					$first=mysqli_query($con,"select * from video_category where Cat_Id=".$pro['Cat_Id']."");
				}else if($i==$k)
				{
					$second=mysqli_query($con,"select * from video_category where Cat_Id=".$pro['Cat_Id']."");   
				}
			}

			$row=mysqli_fetch_array($first,MYSQLI_ASSOC);
			$roww=mysqli_fetch_array($second,MYSQLI_ASSOC);

			mysqli_query($con,"UPDATE video_category SET position=".$row['position']." WHERE Cat_Id=".$roww['Cat_Id']."");  
			mysqli_query($con,"UPDATE video_category SET position=".$roww['position']." WHERE Cat_Id=".$row['Cat_Id']."");  
		}else if($_POST['did']!=0)
		{
			mysqli_query($con,"delete from video_category where Cat_Id = ".$_POST['did']."");
		}
		?>

		<div class="card-body">
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
					<thead>
						<tr>
							<th>Cat_Id</th>
							<th>Category_Name</th>
							<th>Edit</th>
							<th>Delete</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Cat_Id</th>
						<th>Category_Name</th>
						<th>Edit</th>
						<th>Delete</th>
						<th>Position</th>
						<th>Up-Down</th>
					</tr>
				</tfoot>
				<tbody>

					<?php
					$i=0;

					$query=mysqli_query($con,"select * from video_category order by position asc");
					if($query)
					{

						while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {
							$i++;

							?>
							<tr>
								<td><?php echo $row["Cat_Id"];  ?></td>
								<td><?php  echo $row["Category_Name"]; ?></td>

								<td >
									<form action="save_video_category.php" method="POST" style="display: flex;align-items: center;align-content: center;">
										<input type="hidden" name="uid" value="<?php echo $row['Cat_Id']; ?>">
										<div style="margin: 0 auto;">
											<input type="submit" name="edit" value="Edit" class="btn btn-primary btn-block">
										</div>
									</form>
								</td>
								<td style="display: flex;align-items: center;align-content: center;">
									<div style="margin: 0 auto;">
										<button class="btn btn-primary btn-block" value="Delete" name="delete" onclick="get_data('save_video_category','show_video_category',0,'','<?php echo $row['Cat_Id']; ?>');">
											Delete
										</button>
									</div>
								</td>

								<td><?php  echo $row["position"]; ?></td>
								<td style="display: flex;align-items: center;align-content: center;">
									<div style=" display: table;margin: 0 auto;text-align: center;">
										<?php
										if(mysqli_num_rows($query)>1)
										{
											?>
											<div style="display: table-row;">
												<?php
												if($i!=1)
												{
													?>
													<div style=" display: table-cell;padding: 5px;">
														<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_data('save_video_category','show_video_category','<?php echo $row['Cat_Id']; ?>','up',0);">
															<input type="image" src="Images/up.png" />
														</button>
													</div>
													<?php
												}
												?>
												<?php
												if($i!=mysqli_num_rows($query))
												{
													?>
													<div style=" display: table-cell;padding: 5px;">
														<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_data('save_video_category','show_video_category','<?php echo $row['Cat_Id']; ?>','down',0);">
															<input type="image" src="Images/down.png" />
														</button>
													</div>
													<?php
												}
												?>
											</div>
											<?php
										}
										?>
									</div>
								</td>

							</tr>
							<?php
						}
					}
					?>
				</tbody>
			</table>
		</div>
	</div>
	<?php
}
}


if($_POST['page']=="save_video")
{
	if($_POST['work']=="show_video")
	{

		if($_POST['updown']!=0)
		{

			$products=mysqli_query($con,"select * from videos where Cat_Id=".$_POST['cid']." order by position asc");
			$i=0;
			$j=0;
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$i++;
				if($pro['Id']==$_REQUEST['updown'])
				{
					break;
				}
			}
			if($_POST['dir']=="up")
			{
				$products=mysqli_query($con,"select * from videos where Cat_Id=".$_POST['cid']." order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i-1))
					{
						break;
					}
				}
			}else{
				$products=mysqli_query($con,"select * from videos where Cat_Id=".$_POST['cid']." order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i+1))
					{
						break;
					}
				}
			}

			$k=0;
			$products=mysqli_query($con,"select * from videos where Cat_Id=".$_POST['cid']." order by position asc");
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$k++;
				if($j==$k)
				{
					$first=mysqli_query($con,"select * from videos where Id=".$pro['Id']."");
				}else if($i==$k)
				{
					$second=mysqli_query($con,"select * from videos where Id=".$pro['Id']."");   
				}
			}

			$row=mysqli_fetch_array($first,MYSQLI_ASSOC);
			$roww=mysqli_fetch_array($second,MYSQLI_ASSOC);

			mysqli_query($con,"UPDATE videos SET position=".$row['position']." WHERE Id=".$roww['Id']."");  
			mysqli_query($con,"UPDATE videos SET position=".$roww['position']." WHERE Id=".$row['Id']."");  
		}else if($_POST['did']!=0)
		{
			mysqli_query($con,"delete from videos where Id = ".$_POST['did']."");
			mysqli_query($con,"delete from videos_new where Id = ".$_POST['did']."");
	}


		?>
		

		<div class="card-body">
			<div style="display: flex;align-items: center;align-content: center;">
				<div style="margin: 0 auto;width: 250px;">
					<select class="form-control" name="Cat_Id" required="" onchange="get_custom_data('save_video','show_video',0,'',0,this.value);">
						<?php
						$cats=mysqli_query($con,"select * from video_category");
						$first=0;
						while ($cat=mysqli_fetch_array($cats,MYSQLI_ASSOC)) {

							if(isset($_REQUEST['cid']))
							{
								if($cat['Cat_Id']==$_REQUEST['cid'])
								{
									$_SESSION["catid"]=$cat['Cat_Id'];
									?>
									<option value="<?php echo $cat['Cat_Id']; ?>" selected="selected"><?php echo $cat['Category_Name']; ?></option>
									<?php
								}else{
									?>
									<option value="<?php echo $cat['Cat_Id']; ?>"><?php echo $cat['Category_Name']; ?></option>
									<?php
								}
							}else{
								if($first==0)
								{
									$first=1;
									$_SESSION["catid"]=$cat['Cat_Id'];
									?>
									<option value="<?php echo $cat['Cat_Id']; ?>" selected="selected"><?php echo $cat['Category_Name']; ?></option>
									<?php
								}else
								{
									?>
									<option value="<?php echo $cat['Cat_Id']; ?>"><?php echo $cat['Category_Name']; ?></option>
									<?php
								}
							}


						}
						?>
					</select>
				</div>
			</div>


			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
					<thead>
						<tr>
							<th>Id</th>
							<th>Cat_Id</th>
							<th>Theme_Name</th>
							<th>Thumnail_Url</th>
							<th>SoundName</th>
							<th>GameobjectName</th>
							<th>Status</th>
							<th>Edit</th>
							<th>Delete</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th>Id</th>
							<th>Cat_Id</th>
							<th>Theme_Name</th>
							<th>Thumnail_Url</th>
							<th>SoundName</th>
							<th>GameobjectName</th>
							<th>Status</th>
							<th>Edit</th>
							<th>Delete</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</tfoot>
					<tbody>

						<?php
						$i=0;
						$query=mysqli_query($con,"select * from videos where Cat_Id=".$_SESSION["catid"]." order by position asc");
						if($query)
						{

							while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {
								$i++;

								?>
								<tr>
									<td><?php echo $row["Id"];  ?></td>
									<td><?php  echo $row["Cat_Id"]; ?></td>
									<td><?php  echo $row["Theme_Name"]; ?></td>

									<td><?php  echo $row["Thumnail_Url"]; ?></td>
									<td><?php  echo $row["SoundName"]; ?></td>
									<td><?php  echo $row["GameobjectName"]; ?></td>
									<td><?php  echo $row["Status"]; ?></td>
									<td>
										<form action="save_video.php" method="POST" style="display: flex;align-items: center;align-content: center;">
											<input type="hidden" name="uid" value="<?php echo $row['Id']; ?>">
											<div style="margin: 0 auto;">
												<input type="submit" name="edit" value="Edit" class="btn btn-primary btn-block">
											</div>
										</form>
									</td>
									<td style="display: flex;align-items: center;align-content: center;">
										<div style="margin: 0 auto;">
											<button class="btn btn-primary btn-block" value="Delete" name="delete" onclick="get_custom_data('save_video','show_video',0,'','<?php echo $row['Id']; ?>','<?php echo $_SESSION["catid"]; ?>');">
												Delete
											</button>
										</div>
									</td>

									<td><?php  echo $row["position"]; ?></td>
									<td style="display: flex;align-items: center;align-content: center;">
										<div style=" display: table;margin: 0 auto;text-align: center;">
											<?php
											if(mysqli_num_rows($query)>1)
											{
												?>
												<div style="display: table-row;">
													<?php
													if($i!=1)
													{
														?>
														<div style=" display: table-cell;padding: 5px;">
															<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_custom_data('save_video','show_video','<?php echo $row['Id']; ?>','up',0,'<?php echo $_SESSION["catid"]; ?>');">
																<input type="image" src="Images/up.png" />
															</button>
														</div>
														<?php
													}
													?>
													<?php
													if($i!=mysqli_num_rows($query))
													{
														?>
														<div style=" display: table-cell;padding: 5px;">
															<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_custom_data('save_video','show_video','<?php echo $row['Id']; ?>','down',0,'<?php echo $_SESSION["catid"]; ?>');">
																<input type="image" src="Images/down.png" />
															</button>
														</div>
														<?php
													}
													?>
												</div>
												<?php
											}
											?>
										</div>
									</td>
								</tr>
								<?php
							}
						}
						?>
					</tbody>
				</table>
			</div>
		</div>
		<?php
	}

}






if($_POST['page']=="save_video_new")
{
	if($_POST['work']=="show_video_new")
	{

		if($_POST['updown']!=0)
		{
		
			$products=mysqli_query($con,"select * from videos_new order by new_position asc");
			$i=0;
			$j=0;
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$i++;
				if($pro['new_id']==$_REQUEST['updown'])
				{
					break;
				}
			}
			if($_POST['dir']=="up")
			{
				$products=mysqli_query($con,"select * from videos_new order by new_position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i-1))
					{
						break;
					}
				}
			}else{
				$products=mysqli_query($con,"select * from videos_new order by new_position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i+1))
					{
						break;
					}
				}
			}

			$k=0;
			$products=mysqli_query($con,"select * from videos_new order by new_position asc");
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$k++;
				if($j==$k)
				{
					$first=mysqli_query($con,"select * from videos_new where new_id=".$pro['new_id']."");
				}else if($i==$k)
				{
					$second=mysqli_query($con,"select * from videos_new where new_id=".$pro['new_id']."");   
				}
			}

			$row=mysqli_fetch_array($first,MYSQLI_ASSOC);
			$roww=mysqli_fetch_array($second,MYSQLI_ASSOC);

			mysqli_query($con,"UPDATE videos_new SET new_position=".$row['new_position']." WHERE new_id=".$roww['new_id']."");  
			mysqli_query($con,"UPDATE videos_new SET new_position=".$roww['new_position']." WHERE new_id=".$row['new_id']."");  
		}else if($_POST['did']!=0)
		{
			mysqli_query($con,"delete from videos where Id = ".$_POST['did']."");
			mysqli_query($con,"delete from videos_new where Id = ".$_POST['did']."");
		}


		?>
		

		<div class="card-body">

			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
					<thead>
						<tr>
							<th>Id</th>
							<th>Cat_Id</th>
							<th>Theme_Name</th>
							<th>Thumnail_Url</th>
							<th>SoundName</th>
							<th>GameobjectName</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th>Id</th>
							<th>Cat_Id</th>
							<th>Theme_Name</th>
							<th>Thumnail_Url</th>
							<th>SoundName</th>
							<th>GameobjectName</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</tfoot>
					<tbody>

						<?php
						$i=0;
						$query=mysqli_query($con,"select * from videos_new order by new_position asc");
							if($query)
						{

							while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {
								$i++;

								?>
								<tr>
									<td><?php echo $row["Id"];  ?></td>
									<td><?php  echo $row["Cat_Id"]; ?></td>
									<td><?php  echo $row["Theme_Name"]; ?></td>

									<td><?php  echo $row["Thumnail_Url"]; ?></td>
									<td><?php  echo $row["SoundName"]; ?></td>
									<td><?php  echo $row["GameobjectName"]; ?></td>
								

									<td><?php  echo $row["new_position"]; ?></td>
									<td style="display: flex;align-items: center;align-content: center;">
										<div style=" display: table;margin: 0 auto;text-align: center;">
											<?php
											if(mysqli_num_rows($query)>1)
											{
												?>
												<div style="display: table-row;">
													<?php
													if($i!=1)
													{
														?>
															<div style=" display: table-cell;padding: 5px;">
															<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_custom_data('save_video_new','show_video_new','<?php echo $row['new_id']; ?>','up',0,0);">
																<input type="image" src="Images/up.png" />
															</button>
														</div>

														<?php
													}
													?>
													<?php
													if($i!=mysqli_num_rows($query))
													{
														?>
														<div style=" display: table-cell;padding: 5px;">
															<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_custom_data('save_video_new','show_video_new','<?php echo $row['new_id']; ?>','down',0,0);">
																<input type="image" src="Images/down.png" />
															</button>
														</div>
														<?php
													}
													?>
												</div>
												<?php
											}
											?>
										</div>
									</td>
								</tr>
								<?php
							}
						}
						?>
					</tbody>
				</table>
			</div>
		</div>
		<?php
	}

}








if($_POST['page']=="save_sound")
{
	if($_POST['work']=="show_sound")
	{

		if($_POST['updown']!=0)
		{

			$products=mysqli_query($con,"select * from sounds where category_id=".$_POST['cid']." order by position asc");
			$i=0;
			$j=0;
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$i++;
				if($pro['id']==$_REQUEST['updown'])
				{
					break;
				}
			}
			if($_POST['dir']=="up")
			{
				$products=mysqli_query($con,"select * from sounds where category_id=".$_POST['cid']." order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i-1))
					{
						break;
					}
				}
			}else{
				$products=mysqli_query($con,"select * from sounds where category_id=".$_POST['cid']." order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i+1))
					{
						break;
					}
				}
			}

			$k=0;
			$products=mysqli_query($con,"select * from sounds where category_id=".$_POST['cid']." order by position asc");
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$k++;
				if($j==$k)
				{
					$first=mysqli_query($con,"select * from sounds where id=".$pro['id']."");
				}else if($i==$k)
				{
					$second=mysqli_query($con,"select * from sounds where id=".$pro['id']."");   
				}
			}

			$row=mysqli_fetch_array($first,MYSQLI_ASSOC);
			$roww=mysqli_fetch_array($second,MYSQLI_ASSOC);

			mysqli_query($con,"UPDATE sounds SET position=".$row['position']." WHERE id=".$roww['id']."");  
			mysqli_query($con,"UPDATE sounds SET position=".$roww['position']." WHERE id=".$row['id']."");  
		}else if($_POST['did']!=0)
		{
			mysqli_query($con,"delete from sounds where id = ".$_POST['did']."");
		}



		?>
		<div class="card-body">
			<div style="display: flex;align-items: center;align-content: center;">
				<div style="margin: 0 auto;width: 250px;">
					<select class="form-control" name="Cat_Id" required="" onchange="get_custom_data('save_sound','show_sound',0,'',0,this.value);">
						<?php
						$cats=mysqli_query($con,"select * from sound_category");
						$first=0;
						while ($cat=mysqli_fetch_array($cats,MYSQLI_ASSOC)) {
							if(isset($_REQUEST['cid']))
							{
								if($cat['category_id']==$_REQUEST['cid'])
								{
									$_SESSION["category_id"]=$cat['category_id'];
									?>
									<option value="<?php echo $cat['category_id']; ?>" selected="selected"><?php echo $cat['category_name']; ?></option>
									<?php
								}else{
									?>
									<option value="<?php echo $cat['category_id']; ?>"><?php echo $cat['category_name']; ?></option>
									<?php
								}
							}else{
								if($first==0)
								{
									$first=1;
									$_SESSION["category_id"]=$cat['category_id'];
									?>
									<option value="<?php echo $cat['category_id']; ?>" selected="selected"><?php echo $cat['category_name']; ?></option>
									<?php
								}else
								{
									?>
									<option value="<?php echo $cat['category_id']; ?>"><?php echo $cat['category_name']; ?></option>
									<?php
								}
							}


						}
						?>
					</select>
				</div>
			</div>


		
				<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
					<thead>
						<tr>
							<th>id</th>
							<th>category_id</th>
							<th>sound_name</th>

							<th>sound_size</th>
							<th>sound_full_url</th>
							<th>Edit</th>
							<th>Delete</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th>id</th>
							<th>category_id</th>
							<th>sound_name</th>
							<th>sound_size</th>
							<th>sound_full_url</th>
							<th>Edit</th>
							<th>Delete</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</tfoot>
					<tbody>

						<?php
						$query=mysqli_query($con,"select * from sounds where category_id=".$_SESSION["category_id"]." order by position asc");	
						$i=0;
						
						if($query)
						{
							while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {

								$i++;
								?>
								<tr>
									<td><?php echo $row["id"];  ?></td>
									<td><?php  echo $row["category_id"]; ?></td>
									<td><?php  echo $row["sound_name"]; ?></td>

									<td><?php  echo $row["sound_size"]; ?></td>
									<td><?php  echo $row["sound_full_url"]; ?></td>
									<td>
										<form action="save_sound.php" method="POST" style="display: flex;align-items: center;align-content: center;">
											<input type="hidden" name="uid" value="<?php echo $row['id']; ?>">
											<div style="margin: 0 auto;">
												<input type="submit" name="edit" value="Edit" class="btn btn-primary btn-block">
											</div>
										</form>
									</td>
									<td style="display: flex;align-items: center;align-content: center;">
										<div style="margin: 0 auto;">
											<button class="btn btn-primary btn-block" value="Delete" name="delete" onclick="get_custom_data('save_sound','show_sound',0,'','<?php echo $row['id']; ?>','<?php echo $_SESSION["category_id"]; ?>');">
												Delete
											</button>
										</div>
									</td>

									<td><?php  echo $row["position"]; ?></td>
									<td style="display: flex;align-items: center;align-content: center;">
										<div style=" display: table;margin: 0 auto;text-align: center;">
											<?php
											if(mysqli_num_rows($query)>1)
											{
												?>
												<div style="display: table-row;">
													<?php
													if($i!=1)
													{
														?>
														<div style=" display: table-cell;padding: 5px;">
															<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_custom_data('save_sound','show_sound','<?php echo $row['id']; ?>','up',0,'<?php echo $_SESSION["category_id"]; ?>');">
																<input type="image" src="Images/up.png" />
															</button>
														</div>
														<?php
													}
													?>
													<?php
													if($i!=mysqli_num_rows($query))
													{
														?>
														<div style=" display: table-cell;padding: 5px;">
															<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_custom_data('save_sound','show_sound','<?php echo $row['id']; ?>','down',0,'<?php echo $_SESSION["category_id"]; ?>');">
																<input type="image" src="Images/down.png" />
															</button>
														</div>
														<?php
													}
													?>
												</div>
												<?php
											}
											?>
										</div>
									</td>
								</tr>
								<?php
							}
						}
						?>
					</tbody>
				</table>
			</div>
		</div>
		<?php
	}

}


if($_POST['page']=="save_apps")
{
	if($_POST['work']=="show_apps")
	{

		if($_POST['updown']!=0)
		{
			$products=mysqli_query($con,"select * from apps order by position asc");
			$i=0;
			$j=0;
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$i++;
				if($pro['id']==$_POST['updown'])
				{
					break;
				}
			}
			
			if($_POST['dir']=="up")
			{
				$products=mysqli_query($con,"select * from apps order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i-1))
					{
						break;
					}
				}
			}else{
				$products=mysqli_query($con,"select * from apps order by position asc");
				while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
					$j++;
					if($j==($i+1))
					{
						break;
					}
				}
			}


			$k=0;
			$products=mysqli_query($con,"select * from apps order by position asc");
			while ($pro=mysqli_fetch_array($products,MYSQLI_ASSOC)) {
				$k++;
				if($j==$k)
				{
					$first=mysqli_query($con,"select * from apps where id=".$pro['id']."");
				}else if($i==$k)
				{
					$second=mysqli_query($con,"select * from apps where id=".$pro['id']."");   
				}
			}

			$row=mysqli_fetch_array($first,MYSQLI_ASSOC);
			$roww=mysqli_fetch_array($second,MYSQLI_ASSOC);

			mysqli_query($con,"UPDATE apps SET position=".$row['position']." WHERE id=".$roww['id']."");  
			mysqli_query($con,"UPDATE apps SET position=".$roww['position']." WHERE id=".$row['id']."");  
		}else if($_POST['did']!=0)
		{
			mysqli_query($con,"delete from apps where id = ".$_POST['did']."");
		}
		?>

		<div class="card-body">
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
					<thead>
						<tr>
							<th>Id</th>
							<th>Name</th>
							<th>Package</th>
							<th>Url</th>
							<th>Edit</th>
							<th>Delete</th>
							<th>Position</th>
							<th>Up-Down</th>
						</tr>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Id</th>
						<th>Name</th>
						<th>Package</th>
						<th>Url</th>
						<th>Edit</th>
						<th>Delete</th>
						<th>Position</th>
						<th>Up-Down</th>
					</tr>
				</tfoot>
				<tbody>

					<?php
					$i=0;

					$query=mysqli_query($con,"select * from apps order by position asc");
					if($query)
					{

						while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {
							$i++;

							?>
							<tr>
								<td><?php echo $row["id"];  ?></td>
								<td><?php  echo $row["name"]; ?></td>
								<td><?php  echo $row["package"]; ?></td>
								<td><?php  echo $row["url"]; ?></td>

								<td >
									<form action="save_apps.php" method="POST" style="display: flex;align-items: center;align-content: center;">
										<input type="hidden" name="uid" value="<?php echo $row['id']; ?>">
										<div style="margin: 0 auto;">
											<input type="submit" name="edit" value="Edit" class="btn btn-primary btn-block">
										</div>
									</form>
								</td>
								<td style="display: flex;align-items: center;align-content: center;">
									<div style="margin: 0 auto;">
										<button class="btn btn-primary btn-block" value="Delete" name="delete" onclick="get_data('save_apps','show_apps',0,'','<?php echo $row['id']; ?>');">
											Delete
										</button>
									</div>
								</td>

								<td><?php  echo $row["position"]; ?></td>
								<td style="display: flex;align-items: center;align-content: center;">
									<div style=" display: table;margin: 0 auto;text-align: center;">
										<?php
										if(mysqli_num_rows($query)>1)
										{
											?>
											<div style="display: table-row;">
												<?php
												if($i!=1)
												{
													?>
													<div style=" display: table-cell;padding: 5px;">
														<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_data('save_apps','show_apps','<?php echo $row['id']; ?>','up',0);">
															<input type="image" src="Images/up.png" />
														</button>
													</div>
													<?php
												}
												?>
												<?php
												if($i!=mysqli_num_rows($query))
												{
													?>
													<div style=" display: table-cell;padding: 5px;">
														<button type="submit" class="btn btn-primary btn-block" name="up" onclick="get_data('save_apps','show_apps','<?php echo $row['id']; ?>','down',0);">
															<input type="image" src="Images/down.png" />
														</button>
													</div>
													<?php
												}
												?>
											</div>
											<?php
										}
										?>
									</div>
								</td>

							</tr>
							<?php
						}
					}
					?>
				</tbody>
			</table>
		</div>
	</div>
	<?php
}
}




?>