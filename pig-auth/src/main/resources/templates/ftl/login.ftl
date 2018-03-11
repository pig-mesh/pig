<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Pig微服务统一认证</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/signin.css" rel="stylesheet">
  </head>

  <body>
    <div class="container form-margin-top">
      <form class="form-signin" action="/authentication/form" method="post">
        <h2 class="form-signin-heading" align="center">统一认证系统</h2>
        <input type="text" name="username" class="form-control form-margin-top" placeholder="账号" required autofocus>
        <input type="password" name="password" class="form-control" placeholder="密码" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">sign in</button>
      </form>
    </div>
    <footer>
      <p>support by: lengleng</p>
      <p>email: <a href="mailto:wangiegie@gmail.com">wangiegie@gmail.com</a>.</p>
    </footer>
  </body>
</html>
