<#macro masterTemplate title="Welcome"><!DOCTYPE html
            PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<!--Import Google Icon Font-->
		<link
			href="http://fonts.googleapis.com/icon?family=Material+Icons"
			rel="stylesheet">
			<!--Import materialize.css-->
			<link type="text/css" rel="stylesheet"
				href="css/materialize.min.css" media="screen,projection" />
			<link type="text/css" rel="stylesheet" href="css/style.css" />

			<!--Let browser know website is optimized for mobile-->
			<meta name="viewport"
				content="width=device-width, initial-scale=1.0" />
	</head>
	<body class="loaded" style="background-color: #fcfcfc;">
		<div class="wrapper">
			<section>
				<div class="container">
					<div class="section">
						<div class="row">
							<div class="chat_window">
								<div class="top_menu">
									<div class="buttons right">
										<#if user??>
										<ul>

											<li>
												<a href="/logout"
													class="email-type">
													Salir
													[${user.username}]
												</a>
											</li>
										</ul>
										<#else>
										<div class="button" >
												<a href="/register"
													class="email-type">
													Regístrate
												</a>
											</div>
											<div class="button">
												<a href="/login"
													class="email-type">
													Iniciar Sesíon
												</a>
											</div>
										
										</#if>
									</div>
									<div class="title">Chat</div>
								</div>
                                                                    <#nested />
								
							</div>
						</div>
					</div>
				</div>
		</div>
		</section>
		</div>
	</body>
	<script src="js/jquery.2.1.1.min.js"></script>
	<script type="text/javascript" src="js/materialize.min.js"></script>
</html>
</#macro>
