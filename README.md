# Text Analysis Application

A Spring Boot application that finds specific words in large text files by processing them in chunks and using parallel processing for efficient text analysis.

## Features

- **Efficient Text Processing**: Reads large text files in configurable chunks (default: 1000 lines)
- **Parallel Processing**: Uses multithreading for concurrent chunk processing
- **Word Boundary Detection**: Accurate word matching with case-insensitive search
- **REST API**: Simple HTTP endpoint for text analysis requests
- **Robust Error Handling**: Comprehensive exception handling and validation
- **Configurable**: Customizable chunk sizes, thread pools, and processing parameters
- **Docker Support**: Containerized deployment with Docker Compose
- **Monitoring**: Health checks and metrics via Spring Actuator
- **API Documentation**: Interactive API documentation with Swagger/OpenAPI

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker (optional)

### Running Locally

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd text-analysis-app
   ```

2. **Build the application**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   java -jar target/text-analysis-app-1.0.0.jar
   ```

4. **Test the application**
   ```bash
   curl -X POST http://localhost:8080/text-analysis/match \
     -H "Content-Type: application/json" \
     -d '{
       "textUrl": "http://norvig.com/big.txt",
       "words": ["James", "John", "Michael"]
     }'
   ```

### Running with Docker

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up -d
   ```

2. **Access the application**
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/text-analysis/health

## API Usage

### Endpoint

```
POST /text-analysis/match
```

### Request Format

```json
{
  "textUrl": "http://norvig.com/big.txt",
  "words": ["James", "John", "Robert", "Michael"]
}
```

### Response Format

```json
{
  "matches": [
    {
      "word": "james",
      "locations": [
        {
          "lineOffset": 1205,
          "charOffset": 23456
        },
        {
          "lineOffset": 2890,
          "charOffset": 45123
        }
      ]
    }
  ],
  "status": "SUCCESS",
  "processingTimeMs": 2340
}
```

### Example with Most Common Names

```bash
curl -X POST http://localhost:8080/text-analysis/match \
  -H "Content-Type: application/json" \
  -d '{
    "textUrl": "http://norvig.com/big.txt",
    "words": [
      "James", "John", "Robert", "Michael", "William", "David", 
      "Richard", "Charles", "Joseph", "Thomas", "Christopher", 
      "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", 
      "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", 
      "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", 
      "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", 
      "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", "Walter", 
      "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", 
      "Arthur", "Ryan", "Roger"
    ]
  }'
```

## Architecture

The application follows a clean, layered architecture:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controller    │────│    Service      │────│   Utilities     │
│                 │    │                 │    │                 │
│ - REST API      │    │ - Text Reader   │    │ - Text Utils    │
│ - Validation    │    │ - Matcher       │    │ - URL Utils     │
│ - Exception     │    │ - Aggregator    │    │                 │
│   Handling      │    │ - Orchestration │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Key Components

- **TextReaderService**: Downloads and chunks large text files
- **MatcherService**: Performs word matching within text chunks
- **AggregatorService**: Combines and deduplicates results from all matchers
- **TextAnalysisService**: Orchestrates the entire analysis workflow

### Processing Flow

1. **Text Download**: Fetch text content from provided URL
2. **Chunking**: Split text into overlapping chunks (1000 lines with 50-line overlap)
3. **Parallel Processing**: Process chunks concurrently using thread pool
4. **Word Matching**: Search for words using regex with word boundaries
5. **Result Aggregation**: Combine results and eliminate duplicates
6. **Response Formation**: Format final response with locations and metadata

## Configuration

### Application Properties

```yaml
app:
  text-analysis:
    chunk-size: 1000          # Lines per chunk
    chunk-overlap: 50         # Overlap between chunks
    thread-pool-size: 10      # Maximum concurrent threads
    max-file-size: 100MB      # Maximum file size allowed
    request-timeout: 300s     # Request processing timeout

server:
  port: 8080

logging:
  level:
    com.textanalysis: INFO
```

### Environment Variables

- `SPRING_PROFILES_ACTIVE`: Set to `dev`, `prod`, or `test`
- `SERVER_PORT`: Override default port (8080)
- `JAVA_OPTS`: JVM options (e.g., `-Xmx2g -Xms1g`)


## Performance Considerations

### Memory Management

- **Streaming Processing**: Files are processed in chunks to avoid memory issues
- **Configurable Chunk Size**: Adjust based on available memory
- **Garbage Collection**: Optimized object creation and cleanup

### Concurrency

- **Thread Pool**: Configurable thread pool for parallel processing
- **Async Processing**: Non-blocking request handling
- **Thread Safety**: All services are thread-safe

### Optimization Tips

1. **Chunk Size**: Larger chunks reduce overhead but increase memory usage
2. **Thread Pool**: Size should match CPU cores and I/O characteristics  
3. **Overlap**: Minimize overlap while ensuring word boundary accuracy
4. **JVM Tuning**: Use appropriate heap size and GC settings

## Monitoring and Operations

### Logging

- **Structured Logging**: JSON format in production
- **Log Levels**: Configurable per package
- **File Rotation**: Automatic log rotation and archival

### Metrics

- **Processing Time**: Track analysis duration
- **Success/Failure Rates**: Monitor API success rates
- **Memory Usage**: JVM memory metrics
- **Thread Pool**: Monitor thread pool utilization

## Error Handling

The application provides comprehensive error handling:

### Error Types

- **Invalid URL**: Malformed or unreachable URLs
- **File Size Exceeded**: Files larger than configured limit
- **Validation Errors**: Invalid request parameters
- **Processing Errors**: General text analysis failures

### Error Response Format

```json
{
  "error": "INVALID_URL",
  "message": "Unable to access the provided URL",
  "timestamp": "2024-08-17T10:30:45Z"
}
```

## Development

### Development Setup

1. **IDE Setup**: Import as Maven project
2. **Run Configuration**: Set active profile to `dev`
3. **Hot Reload**: Spring DevTools enabled for development
4. **API Testing**: Use Swagger UI at `/swagger-ui.html`

### Code Style

- **Java Code Style**: Follow Google Java Style Guide
- **Documentation**: Comprehensive JavaDoc for public APIs
- **Testing**: Minimum 80% code coverage required
- **Validation**: Input validation on all endpoints

### Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## Security Considerations

- **Input Validation**: All inputs are validated and sanitized
- **URL Restrictions**: Only HTTP/HTTPS URLs are allowed
- **File Size Limits**: Configurable maximum file size
- **Rate Limiting**: Consider adding rate limiting for production use
- **Network Security**: Configure appropriate firewall rules

## Troubleshooting

### Common Issues

1. **OutOfMemoryError**: Reduce chunk size or increase heap size
2. **Connection Timeout**: Increase timeout values or check network connectivity
3. **High CPU Usage**: Reduce thread pool size or chunk processing rate
4. **Slow Performance**: Check file size, network speed, and system resources

### Debug Mode

Enable debug logging:

```yaml
logging:
  level:
    com.textanalysis: DEBUG
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:

- **Issues**: Create a GitHub issue
- **Documentation**: Check the API documentation at `/swagger-ui.html`
- **Logs**: Check application logs for detailed error information
