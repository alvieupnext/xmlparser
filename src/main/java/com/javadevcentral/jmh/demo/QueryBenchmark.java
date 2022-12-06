/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.javadevcentral.jmh.demo;

import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.session.IDocumentSession;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1)
@Fork(value = 1)
@Measurement(iterations = 3)
public class QueryBenchmark {

    @org.openjdk.jmh.annotations.State(Scope.Thread)
    public static class State {
        IDocumentSession session;
        Project p;
        String dbUrl = "http://127.0.0.1:8080";
        String dbName = "Project";

        @Setup(Level.Trial)
        public void setup() {
            p = new Project();
        }
    }

    @Benchmark
    public void E1(State state, Blackhole b) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{state.dbUrl}, state.dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                b.consume(state.p.E1(currentSession));
            }
        }
    }

    @Benchmark
    public void E2(State state, Blackhole b) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{state.dbUrl}, state.dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                b.consume(state.p.E2(currentSession));
            }
        }
    }

    @Benchmark
    public void M1(State state, Blackhole b) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{state.dbUrl}, state.dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                b.consume(state.p.M1(currentSession));
            }
        }
    }

    @Benchmark
    public void M2(State state, Blackhole b) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{state.dbUrl}, state.dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                b.consume(state.p.M2(currentSession));
            }
        }
    }

    @Benchmark
    public void M3(State state, Blackhole b) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{state.dbUrl}, state.dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                b.consume(state.p.M3(currentSession));
            }
        }
    }

    @Benchmark
    public void M4(State state, Blackhole b) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{state.dbUrl}, state.dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                b.consume(state.p.M4(currentSession));
            }
        }
    }

    @Benchmark
    public void M5(State state, Blackhole b) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{state.dbUrl}, state.dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                b.consume(state.p.M5(currentSession));
            }
        }
    }

    @Benchmark
    public void M6(State state, Blackhole b) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{state.dbUrl}, state.dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                b.consume(state.p.M6(currentSession));
            }
        }
    }

    @Benchmark
    public void H1(State state, Blackhole b) {
        try (IDocumentStore store = new DocumentStore(
                new String[]{state.dbUrl}, state.dbName)
        ) {
            DocumentConventions conventions = store.getConventions();
            store.initialize();
            try (IDocumentSession currentSession = store.openSession()) {
                b.consume(state.p.H1(currentSession));
            }
        }
    }
}
