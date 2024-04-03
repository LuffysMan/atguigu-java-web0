# 5. 会话
## Session 保存作用域
- session保存作用域是和具体的某一个session对应的
- 常用API
  - session.setAttribute(k, v)
  - session.getAttribute(k)

# 6. 服务器内部转发以及客户端重定向
1. 服务器内部转发: request.getRequestDispatcher("...").forward(req, resp)
   - 一次请求响应的过程, 客户端不感知内部经过了多少次转发
2. 客户端重定向: response.sendRedirect("...")
   - 两次请求响应的过程. 客户端肯定知道请求URL有变化

# 7. Thymeleaf - 视图模板技术
