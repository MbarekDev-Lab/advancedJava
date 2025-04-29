public class HttpStatusLookup {

    public static void main(String[] args) {
        printHttpStatusMeaning(404);
    }

    public static void printHttpStatusMeaning(int statusCode) {
        switch (statusCode) {
            // 1xx Informational
            case 100: System.out.println("100: Continue"); break;
            case 101: System.out.println("101: Switching Protocols"); break;
            case 102: System.out.println("102: Processing"); break;

            // 2xx Success
            case 200: System.out.println("200: OK"); break;
            case 201: System.out.println("201: Created"); break;
            case 202: System.out.println("202: Accepted"); break;
            case 203: System.out.println("203: Non-Authoritative Information"); break;
            case 204: System.out.println("204: No Content"); break;
            case 205: System.out.println("205: Reset Content"); break;
            case 206: System.out.println("206: Partial Content"); break;

            // 3xx Redirection
            case 300: System.out.println("300: Multiple Choices"); break;
            case 301: System.out.println("301: Moved Permanently"); break;
            case 302: System.out.println("302: Found (Previously Moved Temporarily)"); break;
            case 303: System.out.println("303: See Other"); break;
            case 304: System.out.println("304: Not Modified"); break;
            case 305: System.out.println("305: Use Proxy"); break;
            case 307: System.out.println("307: Temporary Redirect"); break;
            case 308: System.out.println("308: Permanent Redirect"); break;

            // 4xx Client Errors
            case 400: System.out.println("400: Bad Request"); break;
            case 401: System.out.println("401: Unauthorized"); break;
            case 402: System.out.println("402: Payment Required"); break;
            case 403: System.out.println("403: Forbidden"); break;
            case 404: System.out.println("404: Not Found"); break;
            case 405: System.out.println("405: Method Not Allowed"); break;
            case 406: System.out.println("406: Not Acceptable"); break;
            case 407: System.out.println("407: Proxy Authentication Required"); break;
            case 408: System.out.println("408: Request Timeout"); break;
            case 409: System.out.println("409: Conflict"); break;
            case 410: System.out.println("410: Gone"); break;
            case 411: System.out.println("411: Length Required"); break;
            case 412: System.out.println("412: Precondition Failed"); break;
            case 413: System.out.println("413: Payload Too Large"); break;
            case 414: System.out.println("414: URI Too Long"); break;
            case 415: System.out.println("415: Unsupported Media Type"); break;
            case 416: System.out.println("416: Range Not Satisfiable"); break;
            case 417: System.out.println("417: Expectation Failed"); break;
            case 418: System.out.println("418: I'm a teapot"); break; // Fun Easter egg status
            case 429: System.out.println("429: Too Many Requests"); break;

            // 5xx Server Errors
            case 500: System.out.println("500: Internal Server Error"); break;
            case 501: System.out.println("501: Not Implemented"); break;
            case 502: System.out.println("502: Bad Gateway"); break;
            case 503: System.out.println("503: Service Unavailable"); break;
            case 504: System.out.println("504: Gateway Timeout"); break;
            case 505: System.out.println("505: HTTP Version Not Supported"); break;

            default: System.out.println(statusCode + ": Status code not recognized"); 
        }
    }
}
