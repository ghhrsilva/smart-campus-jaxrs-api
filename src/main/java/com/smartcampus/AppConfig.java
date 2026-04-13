package com.smartcampus;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * AppConfig is intentionally not used as the active entry point.
 *
 * This project uses an embedded Grizzly server bootstrapped in Main.java,
 * which registers all resources and filters programmatically via ResourceConfig.
 * In an embedded Grizzly setup, @ApplicationPath is ignored — the base URI is
 * set directly in GrizzlyHttpServerFactory.createHttpServer().
 *
 * This class is kept for reference and completeness.
 */
@ApplicationPath("/api/v1")
public class AppConfig extends Application {
}