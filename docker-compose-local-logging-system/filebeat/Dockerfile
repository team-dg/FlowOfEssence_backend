FROM elastic/filebeat:7.17.24

USER root

COPY ./config/filebeat.yml /usr/share/filebeat/filebeat.yml

RUN groupadd -f docker
RUN usermod -aG docker filebeat

RUN chown filebeat:filebeat /usr/share/filebeat/filebeat.yml

RUN chmod 640 /usr/share/filebeat/filebeat.yml

USER filebeat
