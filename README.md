# FIZ-OAI-Provider

This repository provides an OAI-Provider implementation using [OAICat](https://www.oclc.org/research/areas/data-science/oaicat.html).

## Prerequisites

- Java JDK 21 or higher
- Tomcat 11
- Apache Maven 3
- A running [FIZ-OAI-Backend](https://github.com/ER-FIZKarlsruhe/fiz-oai-backend) instance


## Building the Project
This project uses Maven for build management. Ensure Maven is installed and properly configured.
To build the project, run:

```bash
mvn clean package
```



## Configuration Properties

Below are detailed descriptions of configuration settings available via `oaicat.properties`:

- `OAIHandler.baseURL`
  - Optional: Define the base URL explicitly if your firewall modifies incoming requests.
  ```properties
  OAIHandler.baseURL=http://localhost:8080/oai-provider/OAIHandler
  ```
- `OAIHandler.styleSheet`
  - Include an XSL stylesheet reference to render XML responses into HTML format.
  ```properties
  OAIHandler.styleSheet=/oai/stylesheet
  ```
- `FizOaiBackend.baseURL`
  - URL of the backend
  ```properties
  FizOaiBackend.baseURL=https://oai-backend:8080/oai-backend
  ```

- `AbstractCatalog.oaiCatalogClassName`
  - Defines the OAI catalog implementation class.
  ```properties
  AbstractCatalog.oaiCatalogClassName=de.fiz_karlsruhe.FizOAICatalog
  ```
- `AbstractCatalog.recordFactoryClassName`
  - Defines the record factory implementation class.
  ```properties
  AbstractCatalog.recordFactoryClassName=de.fiz_karlsruhe.FizRecordFactory
  ```
- `AbstractCatalog.secondsToLive`
  - Cache lifetime (in seconds) for responses.
  ```properties
  AbstractCatalog.secondsToLive=3600
  ```
- `AbstractCatalog.granularity`
  - Timestamp granularity for date/time information in OAI responses. Choose one format:
  ```properties
  AbstractCatalog.granularity=YYYY-MM-DD
  #AbstractCatalog.granularity=YYYY-MM-DDThh:mm:ssZ
  ```
- `FizOAICatalog.maxListSize`
  - Maximum number of records returned per request.
  ```properties
  FizOAICatalog.maxListSize=100
  ```
- `FizRecordFactory.repositoryIdentifier`
  - Identifier for your OAI-PMH repository.
  ```properties
  FizRecordFactory.repositoryIdentifier=fiz-karlsruhe.de
  ```
- `FizRecordFactory.defaultMetadataPrefix`
  - Default metadata prefix for records.
  ```properties
  FizRecordFactory.defaultMetadataPrefix=radar
  ```
- `Identify.repositoryName`
  - Human-readable repository name.
  ```properties
  Identify.repositoryName=Radar OAI Repository
  ```
- `Identify.adminEmail`
  - Administrative contact email.
  ```properties
  Identify.adminEmail=mailto:noreply@your-domain.de
  ```
- `Identify.earliestDatestamp`
  - Earliest timestamp available in the repository.
  ```properties
  Identify.earliestDatestamp=2000-01-01T00:00:00Z
  ```
- `Identify.deletedRecord`
  - Policy for deleted records handling (`no`, `transient`, or `persistent`).
  ```properties
  Identify.deletedRecord=no
  ```
- `Identify.repositoryIdentifier`
  - Unique identifier for your repository.
  ```properties
  Identify.repositoryIdentifier=fiz-karlsruhe.de
  ```
- `Identify.sampleIdentifier`
  - Sample identifier demonstrating the repository’s format.
  ```properties
  Identify.sampleIdentifier=oai:fiz-karlsruhe.de:10.0133/10000386
  ```

For further information or customization, refer to [OAICat documentation](https://www.oclc.org/research/areas/data-science/oaicat.html) or contact the repository admin.
  
## Running the Application
Once built, copy the war file into the webapps folder of a Tomcat 11 server.
The oaicat.properties must be placed inside the Tomcat conf folder.
Make sure a FIZ-OAI-Backend instance is running and accessible based on your FizOaiBackend.baseURL configuration.


## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## License

See the `LICENSE` file in the repository for license information.