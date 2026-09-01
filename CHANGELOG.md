# Changelog

## [1.7.0] – 2026-09-01

This release focuses on OAI-PMH spec compliance, hardening against resource leaks and abuse, dependency/security upgrades, and Kubernetes operability. No breaking changes for existing consumers.

### OAI-PMH specification compliance
- A repeated `verb` request parameter is now correctly rejected as `badVerb`, instead of being evaluated as a `badArgument` against whichever value happened to resolve first.
- `Identify` now emits one `<adminEmail>` element per address when `Identify.adminEmail` is configured with a comma-separated list, matching the spec's "one or more" cardinality.
- `Identify` now always emits the required `<granularity>` element, falling back to `YYYY-MM-DD` when unconfigured instead of omitting it.
- `ListSets` now correctly throws `NoSetHierarchyException` when the repository has no sets, instead of returning an empty list.
- Response compression (`gzip`/`deflate`) now honors `q` values in the `Accept-Encoding` header; encodings with unparsable `q` values fail open (treated as fully acceptable) rather than being excluded.

### Reliability and security fixes
- Fixed unbounded growth of `OAIHandler`'s internal attributes cache, which could grow without bound under sustained traffic.
- Fixed a leaked `ScheduledExecutorService` on servlet shutdown/redeploy.
- Fixed a `NullPointerException` in request handling.
- Added security response headers and disabled external DTD loading to prevent XXE attacks.
- XSLT transformation now caches thread-safe `Templates` objects instead of non-thread-safe `Transformer` instances, fixing a latent thread-safety issue and improving throughput.
- Replaced the bundled jQuery 3.4.1 (known vulnerabilities) with 3.7.1.

### Kubernetes / operations
- Added `/health` liveness and readiness endpoints for use as Kubernetes probes, documented in the README.
- `LogoServlet` now supports browser caching.

### Dependencies
- Migrated from Apache HttpClient 4.5.14 (maintenance-only, EOL-track) to HttpClient 5.6.3.
- Bumped Log4j and Netty to address known CVEs.
- Removed unmaintained, manually bundled JARs (`Dbutils`, `Pears`, `SRW`, `xalan`, `xercesImpl`, and others) in favor of managed Maven dependencies; also dropped outdated XML libraries used only by integration tests.

### Quality and test coverage
- Fixed backend exceptions that were previously being swallowed instead of surfaced.
- Reused `HttpClient`, `ObjectMapper`, and compiled `Regex` instances instead of recreating them per request for reduced overhead.
- Removed dead code and resolved outstanding Sonar findings.
- Added Jacoco coverage reporting and substantially increased unit/integration test coverage across the OAI handler, catalog, and record-factory classes.
- Minor UI polish: removed extraneous blank lines from XML rendered in the results textarea.

## [1.6.1] – 2026-12-29
Server-side XSLT rendering for OAI-PMH responses

In version 1.6.1, the browser-side XSLT rendering of OAI-PMH results has been replaced with server-side XSLT rendering.
This change was necessary because Google Chrome has announced the removal of its built-in XSLT engine in 2026, which would otherwise break client-side XML-to-HTML transformations.

### New renderHtml query parameter

The OAI provider now supports a new query parameter:
```
renderHtml=true
```

When this parameter is set, the OAI-PMH XML response is transformed on the server and returned as an HTML representation.
This ensures consistent rendering across all modern browsers and removes the dependency on browser-specific XSLT support.

Existing OAI-PMH consumers that process raw XML programmatically are not affected by this change, as the default response format remains XML unless renderHtml=true is explicitly specified.


## [1.6.0] – 2025-12-12

### Java 25 LTS
The service is running now with Java 25 LTS

### Set resumption token
As defined in the OAI-PMH specification, the ListSets verb supports resumptionToken as an parameter.
Which gives the harvester the possibility to call huge sets lists in multiple steps

https://www.openarchives.org/OAI/openarchivesprotocol.html#ListSets