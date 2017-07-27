package uk.gov.hmrc.sdes.event.service.metrics;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;

public class MetricsServletContextListener implements ServletContextListener {
	
	private final MetricRegistry metricRegistry;
    private final HealthCheckRegistry healthCheckRegistry;

    public MetricsServletContextListener(MetricRegistry metricRegistry, HealthCheckRegistry healthCheckRegistry) {
        this.metricRegistry = metricRegistry;
        this.healthCheckRegistry = healthCheckRegistry;
    }

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		arg0.getServletContext().setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY,healthCheckRegistry);
		arg0.getServletContext().setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);
	    
	}


}
