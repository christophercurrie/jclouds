/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.compute.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.LoadBalancerService;
import org.jclouds.compute.Utils;
import org.jclouds.rest.RestContext;

/**
 * @author Adrian Cole
 */
@Singleton
public class ComputeServiceContextImpl<S, A> implements ComputeServiceContext {
   private final ComputeService computeService;
   private final LoadBalancerService loadBalancerService;
   private final RestContext<S, A> providerSpecificContext;
   private final Utils utils;

   @SuppressWarnings({ "unchecked" })
   @Inject
   public ComputeServiceContextImpl(ComputeService computeService, Utils utils,
            @Nullable LoadBalancerService loadBalancerService, RestContext providerSpecificContext) {
      this.utils = utils;
      this.providerSpecificContext = providerSpecificContext;
      this.computeService = checkNotNull(computeService, "computeService");
      this.loadBalancerService = loadBalancerService;
   }

   public ComputeService getComputeService() {
      return computeService;
   }

   @SuppressWarnings( { "unchecked", "hiding" })
   @Override
   public <S, A> RestContext<S, A> getProviderSpecificContext() {
      return (RestContext<S, A>) providerSpecificContext;
   }

   @Override
   public void close() {
      providerSpecificContext.close();
   }

   @Override
   public LoadBalancerService getLoadBalancerService() {
      return loadBalancerService;
   }

   @Override
   public Utils getUtils() {
      return utils();
   }

   @Override
   public Utils utils() {
      return utils;
   }

   public int hashCode() {
      return providerSpecificContext.hashCode();
   }

   @Override
   public String toString() {
      return providerSpecificContext.toString();
   }

   @Override
   public boolean equals(Object obj) {
      return providerSpecificContext.equals(obj);
   }
}
