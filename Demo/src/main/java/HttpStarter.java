import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.Headers;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.thoughtworks.xstream.XStream;

public class HttpStarter {
    public static void main(String[] args) throws IOException {
        //创建一个HttpServer实例，并绑定到指定的IP地址和端口号
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        //创建一个HttpContext，将路径为/myserver请求映射到MyHttpHandler处理器
        httpServer.createContext("/test", new MyHttpHandler());

        //设置服务器的线程池对象
        httpServer.setExecutor(Executors.newFixedThreadPool(10));

        //启动服务器
        httpServer.start();
    }

    public static class MyHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                StringBuilder responseText = new StringBuilder();
                responseText.append("请求方法：").append(httpExchange.getRequestMethod()).append("<br/>");
                responseText.append("请求参数：").append(getRequestParam(httpExchange)).append("<br/>");

                responseText.append("请求头：<br/>").append(getRequestHeader(httpExchange));
                handleResponse(httpExchange, responseText.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        /**
         * 获取请求头
         * @param httpExchange
         * @return
         */
        public String getRequestHeader(HttpExchange httpExchange) {
            Headers headers = httpExchange.getRequestHeaders();
            return headers.entrySet().stream()
                    .map((Map.Entry<String, List<String>> entry) -> entry.getKey() + ":" + entry.getValue().toString())
                    .collect(Collectors.joining("<br/>"));
        }

        /**
         * 获取请求参数
         * @param httpExchange
         * @return
         * @throws Exception
         */
        public String getRequestParam(HttpExchange httpExchange) throws Exception {
            String paramStr = "";

            if (httpExchange.getRequestMethod().equals("GET")) {
                //GET请求读queryString
                paramStr = httpExchange.getRequestURI().getQuery();
            } else {
                //非GET请求读请求体
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), "utf-8"));
                StringBuilder requestBodyContent = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    requestBodyContent.append(line);
                }
                paramStr = requestBodyContent.toString();
                System.out.println(paramStr);
                xstream(paramStr);
            }

            return paramStr;
        }

        /**
         * 处理响应
         * @param httpExchange
         * @param responsetext
         * @throws Exception
         */
        public void handleResponse(HttpExchange httpExchange, String responsetext) throws Exception {
            //生成html
            StringBuilder responseContent = new StringBuilder();
            responseContent.append("<html>")
                    .append("<body>")
                    .append(responsetext)
                    .append("</body>")
                    .append("</html>");
            String responseContentStr = responseContent.toString();
            byte[] responseContentByte = responseContentStr.getBytes("utf-8");

            //设置响应头，必须在sendResponseHeaders方法之前设置！
            httpExchange.getResponseHeaders().add("Content-Type:", "text/html;charset=utf-8");

            //设置响应码和响应体长度，必须在getResponseBody方法之前调用！
            httpExchange.sendResponseHeaders(200, responseContentByte.length);

            OutputStream out = httpExchange.getResponseBody();
            out.write(responseContentByte);
            out.flush();
            out.close();
        }

        public void xstream(String poc){
            String xml = poc;
            //Class<?>[] classes = new Class[] { java.util.TreeSet.class };
            XStream xstream = new XStream();

            //XStream.setupDefaultSecurity(xstream);
            //xstream.allowTypes(classes);
            xstream.fromXML(xml);
        }

    }
}
