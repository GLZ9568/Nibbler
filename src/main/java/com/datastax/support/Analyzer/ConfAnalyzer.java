/*
 * Copyright (c)
 *
 * Date: 4/1/2018
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.Analyzer;

import com.datastax.support.Util.FileFactory;
import com.datastax.support.Util.NibProperties;
import com.datastax.support.Util.ValFactory;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Chun Gao on 26/11/2017
 */

public class ConfAnalyzer extends Analyzer {

    private final static Logger logger = LogManager.getLogger("ConfAnalyzer.class");
    private ArrayList<NibProperties> cassandraYamlPropertiesList;
    private ArrayList<NibProperties> dseYamlPropertiesList;

    public ConfAnalyzer (FileFactory fileFactory) {
        super(fileFactory);
        this.cassandraYamlPropertiesList = fileFactory.getCassandraYamlPropertiesList();
        this.dseYamlPropertiesList= fileFactory.getDSEYamlPropertiesList();
    }
    /*
    * authenticator: com.datastax.bdp.cassandra.auth.DseAuthenticator
    * authorizer: com.datastax.bdp.cassandra.auth.DseAuthorizer
    * role_manager: com.datastax.bdp.cassandra.auth.DseRoleManager
    * roles_validity_in_ms: 2000
    * permissions_validity_in_ms: 2000
    * seeds: "10.143.99.101,10.143.99.102,10.143.97.192,10.143.97.193,10.143.97.195,10.143.97.196,10.161.99.93,10.161.99.94,10.161.97.192,10.161.97.193,10.161.97.195,10.161.97.196"
    * concurrent_compactors: 6
    * concurrent_reads: 64
    * concurrent_writes: 128
    * tombstone_warn_threshold: 2000
    * tombstone_failure_threshold: 200000
    * read_request_timeout_in_ms: 30000
    * range_request_timeout_in_ms: 10000
    * write_request_timeout_in_ms: 10000
    * streaming_socket_timeout_in_ms: 72000000
    * **/

    TextArea generateConfOutput()
    {
        TextArea t = new TextArea();

        ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(ValFactory.PERCENTILE, ValFactory.READ_LATENCY, ValFactory.WRITE_LATENCY, ValFactory.RANGE_LATENCY));
        String confinfotext = new String();

        String confinfo_warning_header =  new String("#### WARNING: ####\n");

        for(NibProperties np_casyaml: cassandraYamlPropertiesList)
        {
            for(NibProperties np_dseyaml: dseYamlPropertiesList)
            {
                if(np_dseyaml.getProperty(ValFactory.FILE_ID).toString().equals(np_casyaml.getProperty(ValFactory.FILE_ID).toString())){


                }

            }
        }

        return t;
    }
}
