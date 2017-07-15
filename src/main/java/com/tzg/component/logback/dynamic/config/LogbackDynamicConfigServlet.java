package com.tzg.component.logback.dynamic.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet( "/logback/config" )
public class LogbackDynamicConfigServlet extends HttpServlet {

    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
        String logLevel           = req.getParameter( "level" );
        String globalLevel        = req.getParameter( "globalLevel" );
        String qualifiedClassName = req.getParameter( "className" );

        res.setContentType( "text/html" );
        PrintWriter out = res.getWriter();
        out.println( "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">" );
        out.println( "<HTML>" );
        out.println( "  <BODY>" );

        if ( StringUtils.isBlank( logLevel ) || StringUtils.isBlank( qualifiedClassName ) ) {
            out.println( "<H2 style='color: red'>[ 设置失败 ]</H2>" );
            out.println( "<P>[失败原因] 全局类名或者日志级别为空。</P>" );
            out.println( "<P>[使用方法] /logback/config?className=com.tzg.web.foo.controller.FooController&level=info</P>" );
        } else {
            LoggerContext loggerContext = ( LoggerContext ) LoggerFactory.getILoggerFactory();

            /* 设置全局日志级别 */
            if ( !StringUtils.isBlank( globalLevel ) ) {
                Logger rootLogger = loggerContext.getLogger( "root" );
                rootLogger.setLevel( Level.toLevel( globalLevel ) );
            }

            Logger qualifiedClassLogger = loggerContext.getLogger( qualifiedClassName );
            if ( qualifiedClassLogger != null ) {
                qualifiedClassLogger.setLevel( Level.toLevel( logLevel ) );
            }

            out.println( "<H2 style='color: green'>[ 设置成功 ]</H2>" );
            out.println( "<P>[全局类名] " + qualifiedClassName + "</P>" );
            out.println( "<P>[日志级别] " + logLevel + "</P>" );
        }

        out.println( "  </BODY>" );
        out.println( "</HTML>" );

        out.flush();
        out.close();

    }

    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
        doGet( req, res );
    }
}
