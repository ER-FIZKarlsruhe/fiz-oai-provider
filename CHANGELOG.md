# Changelog

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