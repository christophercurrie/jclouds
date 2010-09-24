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

package org.jclouds.compute.callables;

import java.util.Collections;

import javax.inject.Named;

import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.predicates.ScriptStatusReturnsZero.CommandUsingClient;
import org.jclouds.scriptbuilder.InitBuilder;
import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.ssh.ExecResponse;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * 
 * @author Adrian Cole
 */
public class RunScriptOnNode extends InitAndStartScriptOnNode {
   protected final Predicate<CommandUsingClient> runScriptNotRunning;

   public RunScriptOnNode(@Named("SCRIPT_COMPLETE") Predicate<CommandUsingClient> runScriptNotRunning,
            NodeMetadata node, String scriptName, Statement script) {
      this(runScriptNotRunning, node, scriptName, script, true);
   }

   public RunScriptOnNode(@Named("SCRIPT_COMPLETE") Predicate<CommandUsingClient> runScriptNotRunning,
            NodeMetadata node, String scriptName, Statement script, boolean runAsRoot) {
      super(node, scriptName, createInitScript(scriptName, script), runAsRoot);
      this.runScriptNotRunning = runScriptNotRunning;
   }

   public static Statement createInitScript(String scriptName, Statement script) {
      String path = "/tmp/" + scriptName;
      return new InitBuilder(scriptName, path, path, Collections.<String, String> emptyMap(), Collections
               .singleton(script));
   }

   @Override
   public ExecResponse call() {
      ExecResponse returnVal = super.call();

      boolean complete = runScriptNotRunning.apply(new CommandUsingClient("./" + scriptName + " status", ssh));
      logger.debug("<< complete(%s)", complete);
      if (logger.isDebugEnabled() || returnVal.getExitCode() != 0) {
         logger.debug("<< stdout from %s as %s@%s\n%s", scriptName, node.getCredentials().identity, Iterables.get(node
                  .getPublicAddresses(), 0), ssh.exec("./" + scriptName + " tail").getOutput());
         logger.debug("<< stderr from %s as %s@%s\n%s", scriptName, node.getCredentials().identity, Iterables.get(node
                  .getPublicAddresses(), 0), ssh.exec("./" + scriptName + " tailerr").getOutput());
      }
      return returnVal;
   }
}