package com.hubspot.baragon.agent.resources;

import com.google.inject.Inject;
import com.hubspot.baragon.config.LoadBalancerConfiguration;
import com.hubspot.baragon.exceptions.InvalidConfigException;
import com.hubspot.baragon.lbs.LbAdapter;
import com.hubspot.baragon.models.AgentStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {
  private final LbAdapter adapter;
  private final LoadBalancerConfiguration loadBalancerConfiguration;

  @Inject
  public StatusResource(LbAdapter adapter, LoadBalancerConfiguration loadBalancerConfiguration) {
    this.adapter = adapter;
    this.loadBalancerConfiguration = loadBalancerConfiguration;
  }

  @GET
  public AgentStatus getStatus() {
    boolean validConfigs = true;

    try {
      adapter.checkConfigs();
    } catch (InvalidConfigException e) {
      validConfigs = false;
    }

    return new AgentStatus(loadBalancerConfiguration.getName(), validConfigs);
  }
}