## Configuration service that loads application properties via a github repository
### Docker image is created for reusability with following command and environment variables

`docker run --name {container name} -p 8888:8888 -e CONFIG_SERVER_URI={your repository url} -e CONFIG_SERVER_TOKEN={githup personal access token} -d {dockerhub username}/{image name}:{image version e.g 01}`

* docker run 
* --name {container name ayw}
* -p {8888 or ayw}:8888
* -e CONFIG_SERVER_URI={your repository uri}
* -e CONFIG_SERVER_TOKEN={github personal access token}
* -d {dockerhub username}/{image name ayw}:{image version e.g 01}