delete
from batch_step_execution_context
where step_execution_id in
      (select step_execution_id from batch_step_execution where job_execution_id = 8);
delete
from batch_step_execution
where job_execution_id = 8;
delete
from batch_job_execution_params
where job_execution_id = 8;
delete
from batch_job_execution_context
where job_execution_id = 8;
delete
from batch_job_execution
where job_execution_id = 8;