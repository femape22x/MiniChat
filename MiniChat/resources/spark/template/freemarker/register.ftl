<#import "masterTemplate.ftl" as layout />

<@layout.masterTemplate title="Sign In">
<div>
   
 


<form action="/register" method="post">



		<div id="login-page">
			<div class="col s8 margin card-panel"
				style="position: relative;
    margin-left: 18% !important;
    margin-top: 1% !important;">
				
				
				<form class="login-form">

					<div class="row">
						<div class="input-field col s12">
							<i
								class="mdi-social-person-outline prefix">
							</i>
							<input type="text" name="username" maxlength="50" size="30" value="${username!}">
								<label for="username"
									class="center-align">
									Nombre Usuario
								</label>
						</div>
					</div>
					<div class="row ">
						<div class="input-field col s12">
							<i class="mdi-action-lock-outline prefix"></i>
							<input type="text" name="email" maxlength="50" size="30" value="${email!}">
								<label for="email" class="">
									Email
								</label>
						</div>
					</div>
<div class="row ">
						<div class="input-field col s12">
							<i class="mdi-action-lock-outline prefix"></i>
							<input type="password" name="password" maxlength="20" size="30">
								<label for="password" class="">
									Contraseña
								</label>
						</div>
					</div>
<div class="row ">
						<div class="input-field col s12">
							<i class="mdi-action-lock-outline prefix"></i>
							<input type="password" name="password2" maxlength="20" size="30">
								<label for="password2" class="">
									Repetir Contraseña
								</label>
						</div>
					</div>
   <#if error??>
    	<div class="error">
    		<strong>Error:</strong> ${error}
    	</div>
    </#if>
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