import * as React from 'react';
import { PageSection, Title } from '@patternfly/react-core';
import * as Router from 'react-router-dom';

import N from '../../../../platforms/netatmo/ui/build/index.js';

const NC = N(Router);

const Dashboard: React.FunctionComponent<any> = (props) => {
  return (
    <PageSection>
      <Title size="lg">Dashboard Page Title</Title>
      <NC/>
    </PageSection>
  );
}

export { Dashboard };
