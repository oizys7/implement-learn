package com.oizys.study.web.config;

import com.oizys.study.annotation.AutoWrite;
import com.oizys.study.annotation.Component;
import com.oizys.study.annotation.PostConstruct;
import com.oizys.study.web.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.File;
import java.util.logging.LogManager;


/**
 * @author wyn
 * Created on 2025/8/15
 */
@Component
public class TomcatService {
    private static final Logger logger = LoggerFactory.getLogger(TomcatService.class);

    public static final int PORT = 8088;

    @AutoWrite
    private DispatcherServlet dispatcherServlet;

    @PostConstruct
    public void start() throws LifecycleException {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.getConnector();

        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, docBase);

        tomcat.addServlet(contextPath, "dispatcherServlet", dispatcherServlet);
        context.addServletMappingDecoded("/*", "dispatcherServlet");
        try {
            tomcat.start();
            logger.info("Tomcat 'start()' method completed.");

            // 检查 Connector 是否绑定成功
            org.apache.catalina.connector.Connector[] connectors = tomcat.getService().findConnectors();
            boolean isBound = false;
            for (org.apache.catalina.connector.Connector connector : connectors) {
                // 检查端口和绑定状态
                if (connector.getPort() == PORT) {
                    isBound = true;
                    break;
                }
            }

            if (!isBound) {
                logger.error("No connector found for port " + PORT + ". Startup may have failed silently.");
                // 可以在这里抛出异常或返回
                return;
            }

            logger.info("Tomcat started and connector is configured for port " + PORT);

            // 保持运行
            tomcat.getServer().await();

        } catch (Exception e) {
            logger.error("Failed to start Tomcat server:");
            e.printStackTrace();
        }
    }

}
