<#import "masterTemplate.ftl" as layout />

<@layout.masterTemplate title="Timeline">
 <#if user??>



     <div id="chatControls" style="    border-left: 1px solid rgba(0,0,0,0.08);    background-color: #f5f1ee; padding: 0;">
<input id="sec_usuario"  value="${user.username}" style="display:none">
      
       
    </div>
   


	<div class="body">	
<div id="chat" class="messages" style=" padding-left: 9%;
    overflow-y: auto;">    <!-- Built by JS --> </div>

			<div class="bottom_wrapper clearfix">
				<div class="message_input_wrapper">
					<input id="message" class="message_input"
						placeholder="Tu Mensaje aquí..." />
				</div>
 <button id="send" class="send_message">Enviar</button>
				
			</div>
		</div>
		     
</div>	

</#if>
    <script src="js/jquery.2.1.1.min.js"></script>
    <script src="websocketDemo.js"></script>

</@layout.masterTemplate>