package com.mysliborski.tools.exception;

public class TaskCancelledException extends ServiceException {

	private static final long serialVersionUID = 6376797025648154333L;

	public TaskCancelledException() {
		super(ServiceExceptionCodes.TASK_CANCELLED_EXCEPTION);
	}

}
