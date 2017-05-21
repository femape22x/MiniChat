<#import "masterTemplate.ftl" as layout />

 <@layout.masterTemplate title="Sign In"><div>

	<#if message??>
	<div class="success">${message}</div>
	</#if>

	<form action="/login" method="post">



		<div id="login-page">
			<div class="col s8 margin card-panel"
				style="position: relative;
    margin-left: 18% !important;
    margin-top: 5% !important;">
				<h2>Iniciar</h2>
				<#if error??>
				<div class="error">
					<strong>Error:</strong>
					${error}
				</div>
				</#if>
				<form class="login-form">

					<div class="row">
						<div class="input-field col s12">
							<i
								class="mdi-social-person-outline prefix">
							</i>
							<input type="text" name="username" size="30"
								maxlength="50" value="${username!}">
								<label for="username"
									class="center-align">
									Usuario
								</label>
						</div>
					</div>
					<div class="row ">
						<div class="input-field col s12">
							<i class="mdi-action-lock-outline prefix"></i>
							<input type="password" name="password"
								size="30">
								<label for="password" class="">
									Contraseña
								</label>
						</div>
					</div>

					<div class="row">
						<div class="input-field col s12">
							<input type="submit" class="btn "
								value="Sign In">
						</div>
					</div>


				</form>
			</div>
		</div>


	</form>




</div>
</@layout.masterTemplate>
