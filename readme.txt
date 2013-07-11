

web.xml配置：

	<servlet>
		<servlet-name>action</servlet-name>
		<servlet-class>com.janekey.mmvc.action.ActionServlet</servlet-class>
		<load-on-startup>2</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>action</servlet-name>
		<url-pattern>/*</url-pattern>
	</servlet-mapping>