# seasy-docker
docker management and monitor

## seasy-docker-agent  
  部署到Docker Engine服务器节点上，通过访问Docker的APIs来管理和监控服务器节点、镜像和容器等相关信息。  

  容器运行命令参考：  
      docker run -d --name seasy-docker-agent -v /var/run/docker.sock:/var/run/docker.sock -p 5566:5566 192.168.134.139/seasy/seasy-docker-agent:0.0.1  
    
