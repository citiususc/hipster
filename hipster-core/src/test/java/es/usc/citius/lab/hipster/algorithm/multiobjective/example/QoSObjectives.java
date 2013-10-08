/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.lab.hipster.algorithm.multiobjective.example;

public class QoSObjectives {
    public int throughput;
    public double responseTime;

    public QoSObjectives(double responseTime, int throughput) {
        this.throughput = throughput;
        this.responseTime = responseTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(responseTime);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + throughput;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        QoSObjectives other = (QoSObjectives) obj;
        if (Double.doubleToLongBits(responseTime) != Double
                .doubleToLongBits(other.responseTime))
            return false;
        if (throughput != other.throughput)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return responseTime + " ms / " + throughput + " th";
    }


}
